package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.DishFlavor;
import com.yyhdbl.mapper.DishMapper;
import com.yyhdbl.service.DishFlavorService;
import com.yyhdbl.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品 同时保存对应的口味数据
     * 因为要一次性操作两个表，不能直接用save了  得自己重写个方法
     *
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品到菜品表
        this.save(dishDto);
        //由于dishFlavor中还没有存储 dish ID 得取出来重新赋值
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_Falover
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFalvor(Long id) {
        //查询菜品基本信息 从dish表查询
        Dish dish = this.getById(id);
        //查询当前菜品对应的口味信息， 从dish_flavor 表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavorsList = dishFlavorService.list(queryWrapper);
        //由于dish对象中没有flavor字段 只有dishdto里才有 故先将dish拷贝到dishdto中
        // 在把查询出来的口味设置给dishdto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavorsList);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //保存菜品到菜品表
        this.updateById(dishDto);
        //清理当前菜品对应口味数据 用delete操作删除dish_flavor表中的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加之前提交过来的口味数据
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_Falover
        dishFlavorService.saveBatch(flavors);
    }
}
