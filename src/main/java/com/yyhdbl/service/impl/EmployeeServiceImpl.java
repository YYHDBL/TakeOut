package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.entity.Employee;
import com.yyhdbl.mapper.EmployeeMapper;
import com.yyhdbl.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class   EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}
