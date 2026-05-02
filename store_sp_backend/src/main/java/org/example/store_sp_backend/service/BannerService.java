package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.store_sp_backend.entity.Banner;

import java.util.List;

public interface BannerService {
    Page<Banner> pageBanners(Long pageNum, Long pageSize, QueryWrapper<Banner> wrapper);

    List<Banner> listBanners(QueryWrapper<Banner> wrapper);

    void saveBanner(Banner banner);

    void updateBanner(Banner banner);

    void deleteBanner(Long id);
}
