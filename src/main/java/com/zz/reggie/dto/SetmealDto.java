package com.zz.reggie.dto;

import com.zz.reggie.entity.Setmeal;
import com.zz.reggie.entity.SetmealDish;
import com.zz.reggie.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
