package com.zz.reggie.srevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.reggie.entity.User;
import com.zz.reggie.mapper.UserMapper;
import com.zz.reggie.srevice.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
