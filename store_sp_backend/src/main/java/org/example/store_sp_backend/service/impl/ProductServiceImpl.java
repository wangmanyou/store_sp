package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.ProductMapper;
import org.example.store_sp_backend.service.ProductService;
import org.example.store_sp_backend.vo.ProductDetailVO;
import org.example.store_sp_backend.vo.ProductListVO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public PageResponse<ProductListVO> pageProducts(Long categoryId, String keyword, Integer status, Long pageNum, Long pageSize) {
        IPage<ProductListVO> page = productMapper.selectProductPage(new Page<>(pageNum, pageSize), categoryId, keyword, status);
        return PageResponse.from(page);
    }

    @Override
    public ProductDetailVO getProductDetail(Long id, Integer status) {
        ProductDetailVO detail = productMapper.selectProductDetail(id, status);
        if (detail == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "商品不存在");
        }
        return detail;
    }

    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public void saveProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }
}
