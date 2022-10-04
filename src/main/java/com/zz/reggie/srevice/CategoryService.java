package com.zz.reggie.srevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zz.reggie.entity.Category;

/**
 * IService 作用是帮助写了调用相关接口的方法
 */
public interface CategoryService extends IService<Category> {

    public void remove(long id);
}
