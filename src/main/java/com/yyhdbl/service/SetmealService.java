package com.yyhdbl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyhdbl.dto.SetmealDto;
import com.yyhdbl.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐 同时保持套餐与菜品的关系 同时插入两张表
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐 同时删除与菜品的关系
     * @param ids
     */
    public void deleteWithDish(List<Long> ids);




}
