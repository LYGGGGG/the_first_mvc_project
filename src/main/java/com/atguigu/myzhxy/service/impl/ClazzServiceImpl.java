package com.atguigu.myzhxy.service.impl;

/*
@author YG
@create 2022/7/8   0:07
*/

import com.atguigu.myzhxy.mapper.ClazzMapper;
import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getGlazzByOpr(Page<Clazz> pageParam, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("grade_name", gradeName);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        Page<Clazz> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }

    @Override
    public List<Clazz> getClazz() {
        return baseMapper.selectList(null);
    }
}
