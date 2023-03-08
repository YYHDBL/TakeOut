package com.yyhdbl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyhdbl.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
