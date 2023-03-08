package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.R;
import com.yyhdbl.entity.Employee;
import com.yyhdbl.entity.Orders;
import com.yyhdbl.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime) {
        log.info("page ={},pageSize={},number={},beginTime={},endTime={}", page, pageSize, number,beginTime,endTime);
        //构建分页构造器
        Page pageinfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        //添加排序
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageinfo, lambdaQueryWrapper);
        return R.success(pageinfo);
    }

}
