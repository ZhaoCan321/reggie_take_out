package com.zz.reggie.srevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.reggie.entity.Employee;
import com.zz.reggie.mapper.EmployeeMapper;
import com.zz.reggie.srevice.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements  EmployeeService{
}
