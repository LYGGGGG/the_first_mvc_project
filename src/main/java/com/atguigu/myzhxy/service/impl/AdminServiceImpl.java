package com.atguigu.myzhxy.service.impl;

/*
@author YG
@create 2022/7/7   23:22
*/

import com.atguigu.myzhxy.mapper.AdminMapper;
import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (adminName != null) {
            queryWrapper.like("name", adminName);
        }
        queryWrapper.orderByDesc("id");
        Page<Admin> adminPage = baseMapper.selectPage(page, queryWrapper);
        return adminPage;
    }
}
