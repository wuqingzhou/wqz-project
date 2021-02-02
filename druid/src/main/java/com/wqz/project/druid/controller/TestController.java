package com.wqz.project.druid.controller;

import com.wqz.project.druid.mapper.TbUserMapper;
import com.wqz.project.druid.model.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TbUserMapper userMapper;

    @GetMapping("/getUserNum")
    public Integer getUserNum() {
        Integer count = userMapper.selectCount(new TbUser());
        return count;
    }
}
