package com.diploma.order_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diploma.order_service.exceptions.BusinessException;
import com.diploma.order_service.models.order.OrderConfirmation;
import com.diploma.order_service.models.order.OrderLineRequest;
import com.diploma.order_service.models.order.OrderRequest;
import com.diploma.order_service.models.order.OrderResponse;
import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.repository.OrderRepository;
import com.diploma.order_service.requests.CustomerClient;
import com.diploma.order_service.requests.ProductClient;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        // DevNotes: ****************************
        // проверить покупателя (существует ли?) = +
        // оформить товар, т.е. order-service -> product-service = +
        // сохранить заказ в бд = +
        // сохранить строки в заказе = +
        // реализовать процесс оплаты = -
        // + возможно, добавить подтверждение заказа (notification-service), если это
        // уместно будет
        // ****************************

        // check customer
        var customer = customerClient.findById(request.customerId()).orElseThrow(() -> new BusinessException(
                "order creation failed: there is no customer with ID: " + request.customerId()));

        // buy the products
        var buyProducts = productClient.buyProducts(request.products());

        // save order to db
        var order = orderRepository.save(orderMapper.toOrder(request));

        // save order lines to db
        for (BuyRequest buyRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(null, order.getId(), buyRequest.productId(), buyRequest.requestedQuantity()));

        }
        // TODO: create producers & consumers for order-service
        // TODO: payment-service to implement pseudo payment process via kafka

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(request.reference(), request.requestedAmount(), request.paymentMethod(), customer,
                        buyProducts));

        
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::fromOrder).collect(Collectors.toList());

    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("OrderResponse findById:: cant find order by id"));
    }

}
