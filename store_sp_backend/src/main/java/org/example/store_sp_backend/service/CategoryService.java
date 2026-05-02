package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.store_sp_backend.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories(QueryWrapper<Category> wrapper);

    void saveCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Long id);
}
