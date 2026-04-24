package org.example.store_sp_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.store_sp_backend.entity.OrderItem;
import org.example.store_sp_backend.vo.OrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderItemVO> selectItemsByOrderId(@Param("orderId") Long orderId);
}
