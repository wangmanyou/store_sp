package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.store_sp_backend.dto.AddressSaveRequest;
import org.example.store_sp_backend.entity.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> listAddresses(QueryWrapper<UserAddress> wrapper);

    UserAddress getAddressById(Long id);

    UserAddress getOneAddress(QueryWrapper<UserAddress> wrapper);

    void saveOrUpdateAddress(Long userId, AddressSaveRequest request);

    void deleteAddress(Long userId, Long addressId);
}
