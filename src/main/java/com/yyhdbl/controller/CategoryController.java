package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.BaseContext;
import com.yyhdbl.common.R;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Employee;
import com.yyhdbl.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category) {
      Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 菜品分类的分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("page ={},pageSize={}", page, pageSize);
        //构建分页构造器
        Page pageinfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        //按照sort排序
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageinfo, lambdaQueryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 菜品分类的删除操作
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 菜品分类的修改操作
     *
     * @param category
     * @return
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request, @RequestBody Category category) {

        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }


    /**
     * 根据条件查询菜品分类数据
     * 为下拉框提供数据
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categoryList = categoryService.list(lambdaQueryWrapper);
        return R.success(categoryList);
    }






}
