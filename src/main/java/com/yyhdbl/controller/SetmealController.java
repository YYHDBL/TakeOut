package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.R;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.dto.SetmealDto;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.Setmeal;
import com.yyhdbl.entity.SetmealDish;
import com.yyhdbl.service.SetmealDishService;
import com.yyhdbl.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page ={},pageSize={},name={}", page, pageSize, name);
        //构建分页构造器
        Page<Setmeal> pageinfo = new Page(page, pageSize);
        Page<SetmealDto> SetmealDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        //按照sort排序
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        setmealService.page(pageinfo, lambdaQueryWrapper);
     /*   //由于页面想要展示的categoryname在dish中没有 存在于dishDto中 想要将其拷贝过去
        BeanUtils.copyProperties(pageinfo, SetmealDtoPage, "records");
        List<Setmeal> records = pageinfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;//完全听不懂
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);*/
        return R.success(pageinfo);
    }


}
