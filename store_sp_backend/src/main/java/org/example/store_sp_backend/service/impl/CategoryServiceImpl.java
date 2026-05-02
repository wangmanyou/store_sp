package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.entity.Category;
import org.example.store_sp_backend.mapper.CategoryMapper;
import org.example.store_sp_backend.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> listCategories(QueryWrapper<Category> wrapper) {
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public void saveCategory(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }
}
