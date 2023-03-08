package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyhdbl.entity.User;
import com.yyhdbl.mapper.UserMapper;
import com.yyhdbl.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
