package org.example.store_sp_backend.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.StatusUpdateRequest;
import org.example.store_sp_backend.entity.User;
import org.example.store_sp_backend.service.UserService;
import org.example.store_sp_backend.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<UserInfoVO>> list(@RequestParam(defaultValue = "1") Long pageNum,
                                                      @RequestParam(defaultValue = "10") Long pageSize,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Integer status) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().orderByDesc("create_time");
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like("username", keyword).or().like("nickname", keyword).or().like("phone", keyword));
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page = userService.pageUsers(pageNum, pageSize, wrapper);
        List<UserInfoVO> list = page.getRecords().stream().map(user -> {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).toList();
        return ApiResponse.success(new PageResponse<>(page.getTotal(), page.getCurrent(), page.getSize(), list));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserInfoVO> detail(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserInfo(id));
    }

    @PutMapping("/status/{id}")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        User user = new User();
        user.setId(id);
        user.setStatus(request.getStatus());
        userService.updateUser(user);
        return ApiResponse.success("操作成功", null);
    }
}
