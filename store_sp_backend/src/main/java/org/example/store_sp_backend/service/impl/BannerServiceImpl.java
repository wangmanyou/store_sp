package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.entity.Banner;
import org.example.store_sp_backend.mapper.BannerMapper;
import org.example.store_sp_backend.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    @Override
    public Page<Banner> pageBanners(Long pageNum, Long pageSize, QueryWrapper<Banner> wrapper) {
        return bannerMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<Banner> listBanners(QueryWrapper<Banner> wrapper) {
        return bannerMapper.selectList(wrapper);
    }

    @Override
    public void saveBanner(Banner banner) {
        bannerMapper.insert(banner);
    }

    @Override
    public void updateBanner(Banner banner) {
        bannerMapper.updateById(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        bannerMapper.deleteById(id);
    }
}
