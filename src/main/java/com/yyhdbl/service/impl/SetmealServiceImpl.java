package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.common.CustomException;
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

    /**
     * 删除套餐 同时删除与菜品的关系
     *
     * @param ids
     */
    @Override
    public void deleteWithDish(List<Long> ids) {
        //查询套餐状态确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);//select count(*) from setmeal where id in (1,2,3) and status =1
        if (count > 0) {
            //不嫩删除 报出异常
            throw new CustomException("套餐正在售卖中,不能删除");
        }

        //可以删除 删除套餐表的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        //delete * from setmeal_dish where sertmeal_id in(ids)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }
}

