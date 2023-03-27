package com.yyhdbl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyhdbl.entity.OrderDetail;
import com.yyhdbl.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
