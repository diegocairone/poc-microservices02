package com.cairone.orderservice.ctrl;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cairone.orderservice.client.InventoryClient;
import com.cairone.orderservice.dto.OrderDto;
import com.cairone.orderservice.model.Order;
import com.cairone.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderCtrl {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final ExecutorService traceableExecutorService;

    private final StreamBridge streamBridge;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
    
        circuitBreakerFactory.configureExecutorService(traceableExecutorService);
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        
        Supplier<Boolean> supplier = () -> orderDto.getOrderLineItemsList()
                .stream()
                .allMatch(item -> inventoryClient.checkStock(item.getSkuCode()));
        
        boolean allProductsInStock = circuitBreaker.run(
                supplier, throwable -> handleErrorCase(throwable));
        
        if (allProductsInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            
            orderRepository.save(order);

            streamBridge.send(
                    "notificationEventSupplier-out-0", 
                    MessageBuilder.withPayload(order.getId()).build());
            
            return "Order place successfully";
            
        } else {
            return "Order failed; stocks insufficiently";
        }
    }

    private Boolean handleErrorCase(Throwable throwable) {
        log.error(throwable.getMessage());
        return false;
    }
}
