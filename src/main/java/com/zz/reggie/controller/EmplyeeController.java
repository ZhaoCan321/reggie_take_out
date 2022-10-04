package com.zz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zz.reggie.common.R;
import com.zz.reggie.entity.Employee;
import com.zz.reggie.srevice.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmplyeeController {

    @Autowired
     private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        String pwd = employee.getPassword();
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp == null) {
            return R.error("登录失败");
        }

        if(!emp.getPassword().equals(pwd)) {
            return R.error("登录失败");
        }

        if(emp.getStatus() == 0) {
            return R.error("登录失败");
        }

        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
//        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//
//        boolean save = employeeService.save(employee);
//        return R.success("新增成功");

//        自动扩展公共字段
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        boolean save = employeeService.save(employee);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> queryAll(int page,int pageSize,String name) {

//        分页构造器
        Page pageInfo = new Page(page, pageSize);

//        条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee__{}",employee);
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        //        自动扩展公共字段
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);
        if (emp != null) {
            return R.success(emp);
        }
        return R.error("没有查询到对应员工信息");
    }
}
