package com.yyhdbl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.entity.Dish;



public interface DishService extends IService<Dish> {
    public  void saveWithFlavor(DishDto dishDto);


    public  DishDto getByIdWithFalvor(Long id);


    public  void updateWithFlavor(DishDto dishDto);
}
