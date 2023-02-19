package com.yyhdbl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yyhdbl.common.BaseContext;
import com.yyhdbl.common.R;
import com.yyhdbl.entity.Employee;
import com.yyhdbl.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<com.yyhdbl.entity.Employee> login(HttpServletRequest request, @RequestBody com.yyhdbl.entity.Employee employee) {

        //md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<com.yyhdbl.entity.Employee> queryWrapper = new LambdaQueryWrapper<>();
        //eq是判断是否相等并查询，这里是在判断用户输入的用户名与数据库中的是否一致
        queryWrapper.eq(com.yyhdbl.entity.Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null)
            return R.error("用户名不存在");
        if (!emp.getPassword().equals(password))
            return R.error("密码错误");
        if (emp.getStatus() == 0)
            return R.error("员工已禁用");

        //把员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工登出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("员工信息{}", employee.toString());

        //设置初始密码123456，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page ={},pageSize={},name={}", page, pageSize, name);
        //构建分页构造器
        Page pageinfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageinfo, lambdaQueryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 根据id更新员工信息
        启用禁用和编辑员工都用这个方法
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
   /*     employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);*/
        BaseContext.setThreadLocal(empId);
        employeeService.updateById(employee);
        return R.success("员工修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("id={}",id.toString());
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
    return R.error("没有查询到员工信息");
    }





}
