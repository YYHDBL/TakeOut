package com.yyhdbl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yyhdbl.entity.AddressBook;
import com.yyhdbl.mapper.AddressBookMapper;
import com.yyhdbl.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
