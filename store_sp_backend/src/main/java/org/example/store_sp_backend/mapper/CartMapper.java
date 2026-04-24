package org.example.store_sp_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.store_sp_backend.entity.Cart;
import org.example.store_sp_backend.vo.CartItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper extends BaseMapper<Cart> {
    List<CartItemVO> selectCartItemsByUserId(@Param("userId") Long userId);

    Cart selectByUserIdAndProductIdIgnoreDeleted(@Param("userId") Long userId, @Param("productId") Long productId);

    int recoverCart(@Param("id") Long id, @Param("quantity") Integer quantity);

    int addOrUpdateCart(@Param("userId") Long userId,
                        @Param("productId") Long productId,
                        @Param("quantity") Integer quantity);
}
