package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.OrderDeliverRequest;
import org.example.store_sp_backend.dto.OrderSubmitRequest;
import org.example.store_sp_backend.entity.OrderItem;
import org.example.store_sp_backend.entity.Orders;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.entity.UserAddress;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.OrderItemMapper;
import org.example.store_sp_backend.mapper.OrdersMapper;
import org.example.store_sp_backend.service.CartService;
import org.example.store_sp_backend.service.OrdersService;
import org.example.store_sp_backend.service.ProductService;
import org.example.store_sp_backend.service.UserAddressService;
import org.example.store_sp_backend.vo.CartItemVO;
import org.example.store_sp_backend.vo.OrderDetailVO;
import org.example.store_sp_backend.vo.OrderListVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    private static final int ORDER_STATUS_WAIT_DELIVER = 1;
    private static final int ORDER_STATUS_DELIVERED = 2;

    private final CartService cartService;
    private final ProductService productService;
    private final UserAddressService userAddressService;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVO submit(Long userId, OrderSubmitRequest request) {
        UserAddress address = userAddressService.getOne(new QueryWrapper<UserAddress>()
                .eq("id", request.getAddressId())
                .eq("user_id", userId));
        if (address == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "收货地址不存在");
        }

        List<CartItemVO> checkedItems = cartService.listCart(userId).stream()
                .filter(item -> Integer.valueOf(1).equals(item.getChecked()))
                .toList();
        if (checkedItems.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "请选择要结算的商品");
        }

        BigDecimal totalAmount = checkedItems.stream()
                .map(CartItemVO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setAddressId(address.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus(ORDER_STATUS_WAIT_DELIVER);
        order.setRemark(request.getRemark());
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        save(order);

        for (CartItemVO cartItem : checkedItems) {
            Product product = productService.getById(cartItem.getProductId());
            if (product == null || !Integer.valueOf(1).equals(product.getStatus())) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "商品不存在或已下架");
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), product.getName() + " 库存不足");
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            product.setSales((product.getSales() == null ? 0 : product.getSales()) + cartItem.getQuantity());
            productService.updateById(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setProductImage(product.getCoverImage());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItemMapper.insert(orderItem);
        }

        cartService.removeByIds(checkedItems.stream().map(CartItemVO::getCartId).toList());
        return getDetail(order.getId(), userId);
    }

    @Override
    public PageResponse<OrderListVO> pageUserOrders(Long userId, Integer status, Long pageNum, Long pageSize) {
        IPage<OrderListVO> page = baseMapper.selectUserOrderPage(new Page<>(pageNum, pageSize), userId, status);
        return PageResponse.from(page);
    }

    @Override
    public PageResponse<OrderListVO> pageAdminOrders(String orderNo, Integer status, Long pageNum, Long pageSize) {
        IPage<OrderListVO> page = baseMapper.selectAdminOrderPage(new Page<>(pageNum, pageSize), orderNo, status);
        return PageResponse.from(page);
    }

    @Override
    public OrderDetailVO getDetail(Long orderId, Long userId) {
        OrderDetailVO detail = baseMapper.selectOrderDetail(orderId, userId);
        if (detail == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "订单不存在");
        }
        detail.setItems(orderItemMapper.selectItemsByOrderId(orderId));
        return detail;
    }

    @Override
    public void deliver(OrderDeliverRequest request) {
        Orders order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "订单不存在");
        }
        if (!Integer.valueOf(ORDER_STATUS_WAIT_DELIVER).equals(order.getStatus())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "仅待发货订单允许发货");
        }
        order.setStatus(ORDER_STATUS_DELIVERED);
        order.setExpressCompany(request.getExpressCompany());
        order.setExpressNo(request.getExpressNo());
        order.setDeliveryTime(LocalDateTime.now());
        updateById(order);
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
