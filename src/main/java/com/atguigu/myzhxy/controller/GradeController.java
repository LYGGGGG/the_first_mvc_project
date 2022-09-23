package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.GradeService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@author YG
@create 2022/7/8   0:32
*/
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @GetMapping("getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有grade的id的JSON集合")
            @RequestBody List<Integer> ids) {
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改Grade信息，有id是修改，没有是增加")
    @PostMapping("saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("json的grade对象")
            @RequestBody Grade grade) {
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    //sms/gradeController/getGrades/1/3
    @ApiOperation("根据年级名称模糊查询，带分页")
    @GetMapping("getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
         @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
         @ApiParam("分页查询的大小") @PathVariable("pageSize") Integer pageSize,
         @ApiParam("模糊匹配的名称") String gradeName
    ) {
        //分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);

        //封装result对象并返回
        return Result.ok(pageRs);
    }


}
