package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, CartAddRequest request) {
        Product product = productService.getProductById(request.getProductId());
        if (product == null || !Integer.valueOf(1).equals(product.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "商品不存在或已下架");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
        }

        Cart existingCart = cartMapper.selectByUserIdAndProductIdIgnoreDeleted(userId, request.getProductId());
        if (existingCart != null && !Integer.valueOf(1).equals(existingCart.getDeleted())) {
            int newQuantity = existingCart.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
            }
        }
        cartMapper.addOrUpdateCart(userId, request.getProductId(), request.getQuantity());
    }

    @Override
    public List<CartItemVO> listCart(Long userId) {
        return cartMapper.selectCartItemsByUserId(userId);
    }

    @Override
    public void updateCart(Long userId, CartUpdateRequest request) {
        Cart cart = getOwnedCart(userId, request.getCartId());
        if (request.getQuantity() != null) {
            Product product = productService.getProductById(cart.getProductId());
            if (product == null || product.getStock() < request.getQuantity()) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "商品库存不足");
            }
            cart.setQuantity(request.getQuantity());
        }
        if (request.getChecked() != null) {
            cart.setChecked(request.getChecked());
        }
        cartMapper.updateById(cart);
    }

    @Override
    public void deleteCart(Long userId, Long cartId) {
        getOwnedCart(userId, cartId);
        cartMapper.deleteById(cartId);
    }

    @Override
    public void deleteCartItems(List<Long> cartIds) {
        if (cartIds != null && !cartIds.isEmpty()) {
            cartMapper.delete(new QueryWrapper<Cart>().in("id", cartIds));
        }
    }

    private Cart getOwnedCart(Long userId, Long cartId) {
        Cart cart = cartMapper.selectOne(new QueryWrapper<Cart>()
                .eq("id", cartId)
                .eq("user_id", userId));
        if (cart == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "购物车记录不存在");
        }
        return cart;
    }
}
