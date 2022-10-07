package com.zz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zz.reggie.common.R;
import com.zz.reggie.dto.DishDto;
import com.zz.reggie.entity.Category;
import com.zz.reggie.entity.Dish;
import com.zz.reggie.entity.DishFlavor;
import com.zz.reggie.srevice.CategoryService;
import com.zz.reggie.srevice.DishFlavorService;
import com.zz.reggie.srevice.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> pageDtoInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, lambdaQueryWrapper);

//        拷贝对象
        BeanUtils.copyProperties(pageInfo, pageDtoInfo, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        pageDtoInfo.setRecords(list);

        return R.success(pageDtoInfo);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("更新菜品成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
//        先从redis中获取缓存数据，如果存在，直接返回，否则查询数据库，缓存到redis
        List<DishDto> dishDtoList = null;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        dishDtoList = (List<DishDto>) valueOperations.get(key);
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        } else {
            LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//          查询起售状态的菜品
            lambdaQueryWrapper.eq(Dish::getStatus, 1);
            lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
            List<Dish> list = dishService.list(lambdaQueryWrapper);
            dishDtoList = list.stream().map(item -> {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(item, dishDto);
                Long categoryId = item.getCategoryId();
                Category category = categoryService.getById(categoryId);
                if(category != null) {
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                }
                Long dishId = item.getId();
                LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(DishFlavor::getDishId, dishId);
                List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper1);
                dishDto.setFlavors(list1);
                return dishDto;
            }).collect(Collectors.toList());

//            缓存到redis
            valueOperations.set(key, dishDtoList, 60l, TimeUnit.MINUTES);
            return R.success(dishDtoList);
        }


    }

}
