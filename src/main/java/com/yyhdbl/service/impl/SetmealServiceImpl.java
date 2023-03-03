package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.dto.SetmealDto;
import com.yyhdbl.entity.Setmeal;
import com.yyhdbl.entity.SetmealDish;
import com.yyhdbl.mapper.SetmealMapper;
import com.yyhdbl.service.SetmealDishService;
import com.yyhdbl.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保持套餐表 操作setmeal 操作insert
        this.save(setmealDto);
        //菜品套餐关系表里的 套餐id是没有的 需要遍历list 来为这个集合附上setmealID
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联 insert
        setmealDishService.saveBatch(setmealDishes);
    }
}

