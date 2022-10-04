package com.zz.reggie.srevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zz.reggie.dto.SetmealDto;
import com.zz.reggie.entity.Setmeal;

import java.util.List;

/**
 * IService 作用是帮助写了调用相关接口的方法
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
