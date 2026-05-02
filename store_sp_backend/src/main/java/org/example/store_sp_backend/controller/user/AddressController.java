package org.example.store_sp_backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.dto.AddressSaveRequest;
import org.example.store_sp_backend.entity.UserAddress;
import org.example.store_sp_backend.service.UserAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final UserAddressService userAddressService;

    @GetMapping("/list")
    public ApiResponse<List<UserAddress>> list() {
        Long userId = ShiroRealm.getRequiredUserId();
        return ApiResponse.success(userAddressService.listAddresses(new QueryWrapper<UserAddress>()
                .eq("user_id", userId)
                .orderByDesc("is_default")
                .orderByDesc("update_time")));
    }

    @PostMapping("/save")
    public ApiResponse<Void> save(@Valid @RequestBody AddressSaveRequest request) {
        userAddressService.saveOrUpdateAddress(ShiroRealm.getRequiredUserId(), request);
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody AddressSaveRequest request) {
        userAddressService.saveOrUpdateAddress(ShiroRealm.getRequiredUserId(), request);
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userAddressService.deleteAddress(ShiroRealm.getRequiredUserId(), id);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/default/{id}")
    public ApiResponse<Void> setDefault(@PathVariable Long id) {
        AddressSaveRequest request = new AddressSaveRequest();
        UserAddress address = userAddressService.getAddressById(id);
        request.setId(id);
        request.setReceiverName(address.getReceiverName());
        request.setReceiverPhone(address.getReceiverPhone());
        request.setProvince(address.getProvince());
        request.setCity(address.getCity());
        request.setDistrict(address.getDistrict());
        request.setDetailAddress(address.getDetailAddress());
        request.setIsDefault(1);
        userAddressService.saveOrUpdateAddress(ShiroRealm.getRequiredUserId(), request);
        return ApiResponse.success("设置成功", null);
    }
}
