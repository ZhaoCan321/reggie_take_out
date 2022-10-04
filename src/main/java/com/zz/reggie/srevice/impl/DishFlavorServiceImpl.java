package com.zz.reggie.srevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.reggie.entity.DishFlavor;
import com.zz.reggie.mapper.DishFlavorMapper;
import com.zz.reggie.srevice.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
