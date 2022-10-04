package com.zz.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zz.reggie.entity.Setmeal;
import com.zz.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
 * 继承 baseMapper 可以使用mybatis 中的增删改查方法， 如果需要特定的方法需要重写
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

}
