package org.example.store_sp_backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.entity.Banner;
import org.example.store_sp_backend.service.BannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/list")
    public ApiResponse<List<Banner>> list() {
        List<Banner> list = bannerService.listBanners(new QueryWrapper<Banner>()
                .eq("status", 1)
                .orderByAsc("sort"));
        return ApiResponse.success(list);
    }
}
