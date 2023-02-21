package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.common.CustomException;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.Setmeal;
import com.yyhdbl.mapper.CategoryMapper;
import com.yyhdbl.service.CategoryService;
import com.yyhdbl.service.DishService;
import com.yyhdbl.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
   private DishService dishService;
    @Autowired
    private   SetmealService setmealService;


    /**
     * 由于菜品分类和菜品与套餐表有关联，需要先判断是否有关联才能删除
     * 故自定义remove方法进行删除操作
     *
     * @param id
     */
    @Override
    public void remove(Long id) {

        //首先在dish表中查询出当前要删除的菜品分类的id的个数  如果个数>0则说明有关联

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //where categoryId= {菜品分类的id}   Dish::getCategoryId 对应 categoryId   id就是要查的菜品分类id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // select count(*) from dish where categoryId= {菜品分类的id}
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount>0)
        {
            //关联了菜品 抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //首先在setmeal表中查询出当前要删除的菜品分类的id的个数  如果个数>0则说明有关联

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //where categoryId= {菜品分类的id}   Setmeal::getCategoryId 对应 categoryId   id就是要查的菜品分类id
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        // select count(*) from Setmeal where categoryId= {菜品分类的id}
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount>0)
        {
            //关联了菜品 抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }


        super.removeById(id);


    }

}
