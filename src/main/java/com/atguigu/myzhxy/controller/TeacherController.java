package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@author YG
@create 2022/7/8   0:32
*/
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @DeleteMapping("deleteTeacher")
    public Result deleteTeacher(
           @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @RequestBody Teacher teacher
    ) {
        Integer id = teacher.getId();
        if (id == null || id == 0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
            teacherService.saveOrUpdate(teacher);
            return Result.ok();
    }

    @GetMapping("getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @PathVariable Integer pageNo,
            @PathVariable Integer pageSize,
            Teacher teacher
    ) {
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> pageRes = teacherService.getTeachersByOpr(page, teacher);
        return Result.ok(pageRes);
    }
}
