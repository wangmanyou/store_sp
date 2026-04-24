package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.vo.ProductDetailVO;
import org.example.store_sp_backend.vo.ProductListVO;

public interface ProductService extends IService<Product> {
    PageResponse<ProductListVO> pageProducts(Long categoryId, String keyword, Integer status, Long pageNum, Long pageSize);

    ProductDetailVO getProductDetail(Long id, Integer status);
}
