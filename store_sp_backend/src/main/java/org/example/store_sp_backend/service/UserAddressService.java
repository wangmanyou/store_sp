package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.store_sp_backend.dto.AddressSaveRequest;
import org.example.store_sp_backend.entity.UserAddress;

public interface UserAddressService extends IService<UserAddress> {
    void saveOrUpdateAddress(Long userId, AddressSaveRequest request);

    void deleteAddress(Long userId, Long addressId);
}
