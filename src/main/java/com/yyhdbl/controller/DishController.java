package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.BaseContext;
import com.yyhdbl.common.R;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.DishFlavor;
import com.yyhdbl.service.CategoryService;
import com.yyhdbl.service.DishFlavorService;
import com.yyhdbl.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody DishDto dishDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");

    }

    /**
     * 菜品分类的分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page ={},pageSize={},name={}", page, pageSize, name);
        //构建分页构造器
        Page<Dish> pageinfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        //按照sort排序
        lambdaQueryWrapper.orderByAsc(Dish::getUpdateTime);

        dishService.page(pageinfo, lambdaQueryWrapper);
        //由于页面想要展示的categoryname在dish中没有 存在于dishDto中 想要将其拷贝过去
        BeanUtils.copyProperties(pageinfo, dishDtoPage, "records");
        List<Dish> records = pageinfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;//完全听不懂
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应口味信息 用于回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFalvor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody DishDto dishDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
     log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");

    }

}
