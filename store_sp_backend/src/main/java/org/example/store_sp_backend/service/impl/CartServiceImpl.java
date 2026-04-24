package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.CartAddRequest;
import org.example.store_sp_backend.dto.CartUpdateRequest;
import org.example.store_sp_backend.entity.Cart;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.CartMapper;
import org.example.store_sp_backend.service.CartService;
import org.example.store_sp_backend.service.ProductService;
import org.example.store_sp_backend.vo.CartItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, CartAddRequest request) {
        Product product = productService.getById(request.getProductId());
        if (product == null || !Integer.valueOf(1).equals(product.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "商品不存在或已下架");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
        }

        Cart existingCart = baseMapper.selectByUserIdAndProductIdIgnoreDeleted(userId, request.getProductId());
        if (existingCart != null && !Integer.valueOf(1).equals(existingCart.getDeleted())) {
            int newQuantity = existingCart.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
            }
        }
        baseMapper.addOrUpdateCart(userId, request.getProductId(), request.getQuantity());
    }

    @Override
    public List<CartItemVO> listCart(Long userId) {
        return baseMapper.selectCartItemsByUserId(userId);
    }

    @Override
    public void updateCart(Long userId, CartUpdateRequest request) {
        Cart cart = getOwnedCart(userId, request.getCartId());
        if (request.getQuantity() != null) {
            Product product = productService.getById(cart.getProductId());
            if (product == null || product.getStock() < request.getQuantity()) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
            }
            cart.setQuantity(request.getQuantity());
        }
        if (request.getChecked() != null) {
            cart.setChecked(request.getChecked());
        }
        updateById(cart);
    }

    @Override
    public void deleteCart(Long userId, Long cartId) {
        getOwnedCart(userId, cartId);
        removeById(cartId);
    }

    private Cart getOwnedCart(Long userId, Long cartId) {
        Cart cart = getOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getId, cartId)
                .eq(Cart::getUserId, userId));
        if (cart == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "购物车记录不存在");
        }
        return cart;
    }
}
