package com.fmr.service;

import com.fmr.dto.InventoryResponse;
import com.fmr.dto.OrderRequest;
import com.fmr.model.Order;
import com.fmr.model.OrderLineItem;
import com.fmr.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtoList()
                .stream().map(orderLineItemDto ->
                        OrderLineItem.builder()
                                .skuCode(orderLineItemDto.getSkuCode())
                                .price(orderLineItemDto.getPrice())
                                .quantity(orderLineItemDto.getQuantity())
                                .build()
                ).toList();
        order.setOrderLineItemList(orderLineItemList);

        List<String> skuCode = orderLineItemList.stream().map(OrderLineItem::getSkuCode).toList();
        log.info("skuCode ::::::::::{}",skuCode);
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",uriBuilder ->
                    uriBuilder.queryParam("skuCode",skuCode).build())
                        .retrieve().bodyToMono(InventoryResponse[].class)
                        .block();
        log.info("Response ::::::::::{}",inventoryResponses);
        Boolean result = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);
        log.info("Result ::::::::::{}",result);
        if(result) {
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not available in stock please try again later");
        }
        return CompletableFuture.supplyAsync(()->"Order Placed Successfully");
    }
}
