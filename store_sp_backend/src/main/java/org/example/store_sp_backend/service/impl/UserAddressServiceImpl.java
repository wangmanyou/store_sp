package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.AddressSaveRequest;
import org.example.store_sp_backend.entity.UserAddress;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.UserAddressMapper;
import org.example.store_sp_backend.service.UserAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> listAddresses(QueryWrapper<UserAddress> wrapper) {
        return userAddressMapper.selectList(wrapper);
    }

    @Override
    public UserAddress getAddressById(Long id) {
        return userAddressMapper.selectById(id);
    }

    @Override
    public UserAddress getOneAddress(QueryWrapper<UserAddress> wrapper) {
        return userAddressMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateAddress(Long userId, AddressSaveRequest request) {
        if (Integer.valueOf(1).equals(request.getIsDefault())) {
            userAddressMapper.update(null, new UpdateWrapper<UserAddress>()
                    .eq("user_id", userId)
                    .set("is_default", 0));
        }
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(request, address);
        address.setUserId(userId);
        address.setIsDefault(request.getIsDefault() == null ? 0 : request.getIsDefault());
        if (request.getId() == null) {
            userAddressMapper.insert(address);
        } else {
            UserAddress old = userAddressMapper.selectOne(new QueryWrapper<UserAddress>()
                    .eq("id", request.getId())
                    .eq("user_id", userId));
            if (old == null) {
                throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "收货地址不存在");
            }
            userAddressMapper.updateById(address);
        }
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectOne(new QueryWrapper<UserAddress>()
                .eq("id", addressId)
                .eq("user_id", userId));
        if (address == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "收货地址不存在");
        }
        userAddressMapper.deleteById(addressId);
    }
}
