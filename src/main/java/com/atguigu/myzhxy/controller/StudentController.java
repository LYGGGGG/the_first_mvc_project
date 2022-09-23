package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
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
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @DeleteMapping("delStudentById")
    public Result delStudentById(
           @RequestBody List<Integer> ids
    ) {
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @RequestBody Student student
    ) {
        if (student.getId() == null) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @GetMapping("getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable Integer pageNo,
            @PathVariable Integer pageSize,
            Student student
    ) {
        Page<Student> studentPage = new Page<>(pageNo, pageSize);
        IPage<Student> studentRes = studentService.getStudentByOpr(studentPage, student);
        return Result.ok(studentRes);
    }
}
