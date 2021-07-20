package com.cairone.orderservice.ctrl;

import java.util.UUID;

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

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
        
        boolean allProductsInStock = orderDto.getOrderLineItemsList().stream()
                .allMatch(item -> inventoryClient.checkStock(item.getSkuCode()));
        
        if (allProductsInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            
            orderRepository.save(order);
            
            return "Order place successfully";
            
        } else {
            return "Order failed; stocks insufficiently";
        }
    }
}
