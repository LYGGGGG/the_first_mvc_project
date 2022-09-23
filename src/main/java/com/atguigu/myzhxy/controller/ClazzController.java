package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.service.ClazzService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@author YG
@create 2022/7/8   0:32
*/
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @GetMapping("getClazzs")
    public Result getClazzs() {
        List<Clazz> clazzes = clazzService.getClazz();
        return Result.ok(clazzes);
    }

    @ApiOperation("删除单个或多个班级")
    @DeleteMapping("deleteClazz")
    public Result deleteClazz(
            @ApiParam("要删除的多个班级的ID的JSON数组")
            @RequestBody List<Integer> ids) {
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("增加或者修改班级信息")
    @PostMapping("saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息")
            @RequestBody Clazz clazz) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("模糊匹配的名称1") String gradeName,
            @ApiParam("模糊匹配的名称2") String name
    ) {
        //分页 带条件查询
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> pageRs = clazzService.getGlazzByOpr(page, gradeName, name);

        //封装result对象并返回
        return Result.ok(pageRs);
    }

}
