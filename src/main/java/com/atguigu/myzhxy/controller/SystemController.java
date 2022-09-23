package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
@author YG
@create 2022/7/8   14:50
*/
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @PostMapping("updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @PathVariable String oldPwd,
            @PathVariable String newPwd,
            @RequestHeader("token") String token
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().message("登录过期，请重新登录");
        }
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", userId.intValue());
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin == null) {
                    return Result.fail().message("原密码错误");
                }
                admin.setPassword(newPwd);
                adminService.saveOrUpdate(admin);
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student==null){
                    return Result.fail().message("原密码错误");
                }
                student.setPassword(newPwd);
                studentService.saveOrUpdate(student);
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher==null){
                    return Result.fail().message("原密码错误");
                }
                teacher.setPassword(newPwd);
                teacherService.saveOrUpdate(teacher);
                break;
        }
        return Result.ok();
    }

    @PostMapping("headerImgUpload")
    public Result headerImgUpload(
            @RequestPart MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        //uuid替换中文-,获取源文件名，找到.的索引，取后半部分，拼接uuid
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.indexOf(".");
        String newFileName = uuid.concat(originalFilename.substring(i));

        //保存文件的路径
        String portraitPath = "E:/code/myzhxy/target/classes/public/upload/".concat(newFileName);
        //将图片保存到路径
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //响应图片的路径
        String path = "upload/".concat(newFileName);
        return Result.ok(path);
    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", userType);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", userType);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getByTeacherById(userId);
                map.put("userType", userType);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if (sessionVerifiCode.equals("") || sessionVerifiCode == null) {
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if (!loginVerifiCode.equalsIgnoreCase(sessionVerifiCode)) {
            return Result.fail().message("验证码有误,请重新输入");
        }
        session.removeAttribute("verifiCode");

        //到这里说明验证码正确，检验是否存在用户
        Map<String, Object> map = new HashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                Admin admin = adminService.login(loginForm);
                try {
                    if (admin != null) {
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                Student student = studentService.login(loginForm);
                try {
                    if (student != null) {
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                Teacher teacher = teacherService.login(loginForm);
                try {
                    if (teacher != null) {
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        //将验证码图片响应给浏览器
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(verifiCodeImage, "JPEG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
