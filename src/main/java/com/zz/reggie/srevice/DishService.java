package com.zz.reggie.srevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zz.reggie.dto.DishDto;
import com.zz.reggie.entity.Dish;

/**
 * IService 作用是帮助写了调用相关接口的方法
 */
public interface DishService extends IService<Dish> {

    //    新增菜品，同时插入口味数据，需要操作两张表： dish dishFlavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);

}
