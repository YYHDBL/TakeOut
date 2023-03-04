package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyhdbl.common.BaseContext;
import com.yyhdbl.common.R;
import com.yyhdbl.dto.DishDto;
import com.yyhdbl.dto.SetmealDto;
import com.yyhdbl.entity.Category;
import com.yyhdbl.entity.Dish;
import com.yyhdbl.entity.Setmeal;
import com.yyhdbl.entity.SetmealDish;
import com.yyhdbl.service.CategoryService;
import com.yyhdbl.service.SetmealDishService;
import com.yyhdbl.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page ={},pageSize={},name={}", page, pageSize, name);
        //构建分页构造器 按照page和pagsize条件查询带回来的结果放在pageinfo里
        Page<Setmeal> pageinfo = new Page(page, pageSize);

        //构造条件构造器  这块得放在前面 因为page查询回来的东西放在了pageinfo里   后面要有东西才能拷贝
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        //按照sort排序
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo, lambdaQueryWrapper);


        //由于页面要展示套餐分类的名字 而在setmeal里没有 得用setmealdto
        // 于是创建个泛型为setmealdto的page对象
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        //但是setmealdto是个空对象 里面没值啊，查询回来的值在pageinfo里
        //于是要把pageinfo里的属性拷贝到setmealdtopage里
        //但是page里面records这个属性不要拷贝 因为setmeal和setmealdto的泛型不一样 我们得手动拷贝过去
        BeanUtils.copyProperties(pageinfo, setmealDtoPage, "records");

        //取出pageinfo里的records
        List<Setmeal> records = pageinfo.getRecords();
        //用stream流遍历records     item就是records这个list里的每一项
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            //创建个setmealdto对象 把每项item拷贝给它  但是还缺个categoriesname
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            //从item拿到categoriesID
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //把获取到的categoryname赋给setmealdto对象
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());//收集起来


        setmealDtoPage.setRecords(setmealDtos);//把重新组装好的setmealdto给setmealdtopage

        return R.success(setmealDtoPage);
    }

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto, HttpServletRequest request) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }


    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("套餐数据删除成功");
    }


    /**
     * 起售
     */
    @PostMapping("/status/0")
    public R<String> updateStatus0(@RequestParam List<Long> ids, HttpServletRequest request) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(0);
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);

        setmealService.update(setmeal, lambdaQueryWrapper);
        return R.success("禁售成功");
    }
    /**
     * 禁售
     */
    @PostMapping("/status/1")
    public R<String> updateStatus1(@RequestParam List<Long> ids, HttpServletRequest request) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(1);
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        setmealService.update(setmeal, lambdaQueryWrapper);
        return R.success("启售成功");
    }

}
