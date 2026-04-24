package org.example.store_sp_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.vo.ProductDetailVO;
import org.example.store_sp_backend.vo.ProductListVO;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper extends BaseMapper<Product> {
    IPage<ProductListVO> selectProductPage(Page<ProductListVO> page,
                                           @Param("categoryId") Long categoryId,
                                           @Param("keyword") String keyword,
                                           @Param("status") Integer status);

    ProductDetailVO selectProductDetail(@Param("id") Long id, @Param("status") Integer status);
}
