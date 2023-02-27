package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.BaseContext;
import com.yyhdbl.common.R;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.DishFlavor;
import com.yyhdbl.service.DishFlavorService;
import com.yyhdbl.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        Page pageinfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        //按照sort排序
        lambdaQueryWrapper.orderByAsc(Dish::getSort);
        dishService.page(pageinfo, lambdaQueryWrapper);
        return R.success(pageinfo);
    }
}
