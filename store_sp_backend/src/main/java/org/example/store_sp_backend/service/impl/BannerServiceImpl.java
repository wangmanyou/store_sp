package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.store_sp_backend.entity.Banner;
import org.example.store_sp_backend.mapper.BannerMapper;
import org.example.store_sp_backend.service.BannerService;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
