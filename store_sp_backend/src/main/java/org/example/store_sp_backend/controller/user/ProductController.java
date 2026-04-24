package org.example.store_sp_backend.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.service.ProductService;
import org.example.store_sp_backend.vo.ProductDetailVO;
import org.example.store_sp_backend.vo.ProductListVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductListVO>> list(@RequestParam(required = false) Long categoryId,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(defaultValue = "1") Long pageNum,
                                                         @RequestParam(defaultValue = "10") Long pageSize) {
        return ApiResponse.success(productService.pageProducts(categoryId, keyword, 1, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(productService.getProductDetail(id, 1));
    }
}
