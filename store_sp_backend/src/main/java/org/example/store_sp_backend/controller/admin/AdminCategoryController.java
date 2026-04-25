package org.example.store_sp_backend.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.dto.CategorySaveRequest;
import org.example.store_sp_backend.entity.Category;
import org.example.store_sp_backend.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ApiResponse<List<Category>> list(@RequestParam(required = false) Integer status) {
        QueryWrapper<Category> wrapper = new QueryWrapper<Category>().orderByAsc("sort");
        if (status != null) {
            wrapper.eq("status", status);
        }
        return ApiResponse.success(categoryService.list(wrapper));
    }

    @PostMapping("/save")
    public ApiResponse<Void> save(@Valid @RequestBody CategorySaveRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        category.setCreateBy(ShiroRealm.getRequiredAdminId());
        categoryService.save(category);
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody CategorySaveRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        categoryService.updateById(category);
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return ApiResponse.success("删除成功", null);
    }
}
