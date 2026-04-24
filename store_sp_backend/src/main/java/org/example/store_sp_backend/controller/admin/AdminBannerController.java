package org.example.store_sp_backend.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.AuthContext;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.BannerSaveRequest;
import org.example.store_sp_backend.dto.StatusUpdateRequest;
import org.example.store_sp_backend.entity.Banner;
import org.example.store_sp_backend.service.BannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<Banner>> list(@RequestParam(defaultValue = "1") Long pageNum,
                                                  @RequestParam(defaultValue = "10") Long pageSize,
                                                  @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<Banner>().orderByAsc(Banner::getSort);
        if (status != null) {
            wrapper.eq(Banner::getStatus, status);
        }
        return ApiResponse.success(PageResponse.from(bannerService.page(new Page<>(pageNum, pageSize), wrapper)));
    }

    @PostMapping("/save")
    public ApiResponse<Void> save(@Valid @RequestBody BannerSaveRequest request) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(request, banner);
        banner.setCreateBy(AuthContext.getRequiredAdminId());
        bannerService.save(banner);
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody BannerSaveRequest request) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(request, banner);
        bannerService.updateById(banner);
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/status/{id}")
    public ApiResponse<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setStatus(request.getStatus());
        bannerService.updateById(banner);
        return ApiResponse.success("状态更新成功", null);
    }
}
