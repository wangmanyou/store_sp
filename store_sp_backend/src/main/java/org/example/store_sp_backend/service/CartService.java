package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.store_sp_backend.dto.CartAddRequest;
import org.example.store_sp_backend.dto.CartUpdateRequest;
import org.example.store_sp_backend.entity.Cart;
import org.example.store_sp_backend.vo.CartItemVO;

import java.util.List;

public interface CartService extends IService<Cart> {
    void add(Long userId, CartAddRequest request);

    List<CartItemVO> listCart(Long userId);

    void updateCart(Long userId, CartUpdateRequest request);

    void deleteCart(Long userId, Long cartId);
}
