package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@author YG
@create 2022/7/8   0:32
*/
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @DeleteMapping("deleteAdmin")
    public Result deleteAdmin(
            @RequestBody List<Integer> ids
    ) {
        adminService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @RequestBody Admin admin
    ) {
        Integer id = admin.getId();
        if (id == null || id == 0) {
            admin.setPassword(MD5.encrypt((admin.getPassword())));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @GetMapping("getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable Integer pageNo,
            @PathVariable Integer pageSize,
            String adminName
    ) {
        Page<Admin> page = new Page<>(pageNo, pageSize);
        IPage<Admin> pageRes = adminService.getAdminByOpr(page, adminName);
        return Result.ok(pageRes);
    }
}
