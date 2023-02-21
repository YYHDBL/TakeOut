package com.yyhdbl.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<com.yyhdbl.entity.Category> {
    public  void remove(Long id);
}
