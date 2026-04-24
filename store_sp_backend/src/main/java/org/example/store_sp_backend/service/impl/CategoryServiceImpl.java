package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.store_sp_backend.entity.Category;
import org.example.store_sp_backend.mapper.CategoryMapper;
import org.example.store_sp_backend.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
