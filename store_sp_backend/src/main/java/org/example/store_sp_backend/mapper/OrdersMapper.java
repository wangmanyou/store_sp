package org.example.store_sp_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.store_sp_backend.entity.Orders;
import org.example.store_sp_backend.vo.OrderDetailVO;
import org.example.store_sp_backend.vo.OrderListVO;
import org.apache.ibatis.annotations.Param;

public interface OrdersMapper extends BaseMapper<Orders> {
    IPage<OrderListVO> selectUserOrderPage(Page<OrderListVO> page,
                                           @Param("userId") Long userId,
                                           @Param("status") Integer status);

    IPage<OrderListVO> selectAdminOrderPage(Page<OrderListVO> page,
                                            @Param("orderNo") String orderNo,
                                            @Param("status") Integer status);

    OrderDetailVO selectOrderDetail(@Param("id") Long id, @Param("userId") Long userId);
}
