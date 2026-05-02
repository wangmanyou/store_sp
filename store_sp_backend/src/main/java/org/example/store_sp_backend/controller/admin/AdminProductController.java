package org.example.store_sp_backend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.ProductSaveRequest;
import org.example.store_sp_backend.dto.StatusUpdateRequest;
import org.example.store_sp_backend.entity.Product;
import org.example.store_sp_backend.service.ProductService;
import org.example.store_sp_backend.vo.ProductDetailVO;
import org.example.store_sp_backend.vo.ProductListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductListVO>> list(@RequestParam(defaultValue = "1") Long pageNum,
                                                         @RequestParam(defaultValue = "10") Long pageSize,
                                                         @RequestParam(required = false) Long categoryId,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) Integer status) {
        return ApiResponse.success(productService.pageProducts(categoryId, keyword, status, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(productService.getProductDetail(id, null));
    }

    @PostMapping("/save")
    public ApiResponse<Void> save(@Valid @RequestBody ProductSaveRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        product.setCreateBy(ShiroRealm.getRequiredAdminId());
        product.setSales(0);
        productService.saveProduct(product);
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody ProductSaveRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        productService.updateProduct(product);
        return ApiResponse.success("修改成功", null);
    }

    @PutMapping("/status/{id}")
    public ApiResponse<Void> status(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(request.getStatus());
        productService.updateProduct(product);
        return ApiResponse.success("状态更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success("删除成功", null);
    }
}
