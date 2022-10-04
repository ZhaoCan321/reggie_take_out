package com.zz.reggie.srevice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.reggie.common.CustomException;
import com.zz.reggie.entity.Category;
import com.zz.reggie.entity.Dish;
import com.zz.reggie.entity.Setmeal;
import com.zz.reggie.mapper.CategoryMapper;
import com.zz.reggie.srevice.CategoryService;
import com.zz.reggie.srevice.DishService;
import com.zz.reggie.srevice.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(long id) {
//      当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(lambdaQueryWrapper);
        if(count > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
//      当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if(setmealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        this.removeById(id);
    }
}
