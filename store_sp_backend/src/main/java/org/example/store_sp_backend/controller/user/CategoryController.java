package org.example.store_sp_backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.entity.Category;
import org.example.store_sp_backend.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ApiResponse<List<Category>> list() {
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort));
        return ApiResponse.success(list);
    }
}
