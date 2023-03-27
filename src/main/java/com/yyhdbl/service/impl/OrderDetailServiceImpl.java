package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.entity.OrderDetail;
import com.yyhdbl.mapper.OrderDetailMapper;
import com.yyhdbl.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.security.Provider;
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
