package com.bookshop.service.impl;

import com.bookshop.dto.order.OrderRequestDto;
import com.bookshop.dto.order.OrderResponseDto;
import com.bookshop.dto.orderitem.OrderItemResponseDto;
import com.bookshop.exception.CreateOrderException;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.OrderItemMapper;
import com.bookshop.mapper.OrderMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.Order;
import com.bookshop.model.OrderItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.OrderItemRepository;
import com.bookshop.repository.OrderRepository;
import com.bookshop.repository.ShoppingCartRepository;
import com.bookshop.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(User user, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("There is no user with the id: "
                                + user.getId())
                );
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new CreateOrderException("There is nothing in the shopping cart.");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.CREATED);
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setOrderItems(createOrderItems(cartItems, order));
        order.setTotal(countTotalSum(order.getOrderItems()));
        shoppingCart.getCartItems().clear();
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllOrdersByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemResponseDto> findAllByOrderId(Long orderId,
                                                       Pageable pageable,
                                                       Long userId) {
        Order order = checkIfOrderExistsById(orderId);
        checkIfOrderBelongsToUser(order.getUser().getId(), userId, orderId);
        return orderItemRepository.findAllByOrderId(orderId, pageable)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItemByIdInOrder(Long orderId,
                                                         Long orderItemId,
                                                         Long userId) {
        Order order = checkIfOrderExistsById(orderId);
        checkIfOrderBelongsToUser(order.getUser().getId(), userId, orderId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no order item with the id: " + orderItemId)
        );
        if (orderItem.getOrder().getId().equals(orderId)) {
            return orderItemMapper.toDto(orderItem);
        } else {
            throw new EntityNotFoundException(
                    "There is no order item with the id: %d in your order"
                            .formatted(orderItemId));
        }
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String newStatus) {
        Order order = checkIfOrderExistsById(orderId);
        order.setStatus(Order.Status.valueOf(newStatus));
        return orderMapper.toDto(order);
    }

    private Order checkIfOrderExistsById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("There is no order with id: " + orderId)
        );
    }

    private void checkIfOrderBelongsToUser(Long userIdFromRequest,
                                           Long actualUserId,
                                           Long orderId) {
        if (!userIdFromRequest.equals(actualUserId)) {
            throw new EntityNotFoundException("There is no order with id %d in your order history"
                    .formatted(orderId));
        }
    }

    private Set<OrderItem> createOrderItems(Set<CartItem> cartItems, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            cartItem.setShoppingCart(null);
        }
        return orderItems;
    }

    private BigDecimal countTotalSum(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
