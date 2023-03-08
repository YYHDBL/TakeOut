package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.entity.Orders;
import com.yyhdbl.mapper.OrderMapper;
import com.yyhdbl.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
}
