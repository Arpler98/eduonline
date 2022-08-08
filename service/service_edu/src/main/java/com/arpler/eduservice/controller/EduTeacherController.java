package com.arpler.eduservice.controller;


import com.arpler.commonutils.R;
import com.arpler.eduservice.entity.EduTeacher;
import com.arpler.eduservice.entity.vo.TeacherQuery;
import com.arpler.eduservice.service.EduTeacherService;
import com.arpler.servicebase.exception.EduException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author arpler
 * @since 2022-02-19
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

//    访问地址： http://localhost:8001/eduservice/teacher/findAll
//    注入service
    @Autowired
    private EduTeacherService eduTeacherService;
//    1.查询讲师表所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
//        调用service方法
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

//    2.逻辑删除讲师
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name="id",value = "讲师id",required = true) @PathVariable String id){
        boolean b = eduTeacherService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

//    3.分页查询讲师
//    current:当前页
//    limit:每页显示的记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit){
        Page<EduTeacher> eduTeacherPage=new Page<>(current,limit);
//        调用方法实现分页
        eduTeacherService.page(eduTeacherPage,null);
        long total = eduTeacherPage.getTotal();//总记录数
        List<EduTeacher> records = eduTeacherPage.getRecords();//数据list集合

//        Map map=new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("rows",records);
    }

    //4 条件查询带分页
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> page=new Page<>(current,limit);
        QueryWrapper<EduTeacher> queryWrapper=new QueryWrapper<>();
        //多条件组合查询
        String name=teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_create",end);
        }
        //调用方法实现条件查询分页
        eduTeacherService.page(page,queryWrapper);
        long total = page.getTotal();//总记录数
        List<EduTeacher> records = page.getRecords();//数据list集合

        return R.ok().data("total",total).data("rows",records);
    }

    //5 添加讲师
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //6 根据讲师id查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher byId = eduTeacherService.getById(id);
        return R.ok().data("teacher",byId);
    }

    //7 修改讲师
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

