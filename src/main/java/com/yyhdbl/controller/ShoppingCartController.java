package com.yyhdbl.controller;


import com.yyhdbl.common.R;
import com.yyhdbl.entity.ShoppingCart;
import com.yyhdbl.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("{}",shoppingCart);
        return null;
    }

}
