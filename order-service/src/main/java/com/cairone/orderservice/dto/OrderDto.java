package com.cairone.orderservice.dto;

import java.util.List;

import com.cairone.orderservice.model.OrderLineItems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    
    private List<OrderLineItems> orderLineItemsList;
}
