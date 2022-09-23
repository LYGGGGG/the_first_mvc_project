package com.atguigu.myzhxy.service;

/*
@author YG
@create 2022/7/8   0:06
*/

import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.pojo.Grade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getGlazzByOpr(Page<Clazz> page, String gradeName,String name);

    List<Clazz> getClazz();
}
