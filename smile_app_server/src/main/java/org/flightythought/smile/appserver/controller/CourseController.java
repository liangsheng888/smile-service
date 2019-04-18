package org.flightythought.smile.appserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.flightythought.smile.appserver.bean.CourseSimple;
import org.flightythought.smile.appserver.bean.ResponseBean;
import org.flightythought.smile.appserver.bean.SelectItemOption;
import org.flightythought.smile.appserver.database.entity.UserFollowCourseEntity;
import org.flightythought.smile.appserver.dto.ApplyCourseDTO;
import org.flightythought.smile.appserver.dto.CourseInfoQueryDTO;
import org.flightythought.smile.appserver.dto.CourseQueryDTO;
import org.flightythought.smile.appserver.dto.PageFilterDTO;
import org.flightythought.smile.appserver.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/course")
@Api(tags = "相关课程控制层", description = "获取相关课程相关接口")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    @ApiOperation(value = "获取相关课程", notes = "可以根据解决方案ID或课程ID获取相关课程，不传任何参数可获取全部课程")
    @PostMapping("/results")
    public ResponseBean getCourses(@RequestBody CourseQueryDTO courseQueryDTO) {
        try {
            Page<CourseSimple> result = courseService.getCourseSimples(courseQueryDTO);
            return ResponseBean.ok("返回成功", result);
        } catch (Exception e) {
            LOG.error("获取相关课程失败", e);
            return ResponseBean.error("返回失败", e.getMessage());
        }
    }

    @ApiOperation(value = "根据时间区间、分页获取课程", notes = "可以根据时间区间、分类、分页获取课程，也可以获取全部课程（typeId为0获取其他课程类型）")
    @PostMapping("/info")
    public ResponseBean getCoursesInfo(@RequestBody CourseInfoQueryDTO courseInfoQueryDTO) {
        try {
            Page<CourseSimple> result = courseService.getCoursesInfo(courseInfoQueryDTO);
            return ResponseBean.ok("返回成功", result);
        } catch (Exception e) {
            LOG.error("获取课程失败", e);
            return ResponseBean.error("返回失败", e.getMessage());
        }
    }

    @ApiOperation(value = "获取课程类型", notes = "获取课程类型")
    @PostMapping("/courseType")
    public ResponseBean getCoursesType(@RequestBody PageFilterDTO pageFilterDTO) {
        try {
            Page<SelectItemOption> result = courseService.getCourseType(pageFilterDTO);
            return ResponseBean.ok("获取课程类型成功", result);
        } catch (Exception e) {
            LOG.error("获取课程类型失败", e);
            return ResponseBean.error("获取课程类型失败", e.getMessage());
        }
    }

    @ApiOperation(value = "预约报名课程", notes = "预约报名课程，发送短信验证码可调用登录时候的请求验证码接口")
    @PostMapping("/applyCourse")
    public ResponseBean applyCourse(@RequestBody ApplyCourseDTO applyCourseDTO) {
        try {
            UserFollowCourseEntity result = courseService.applyCourse(applyCourseDTO);
            return ResponseBean.ok("预约报名课程成功", result);
        } catch (Exception e) {
            LOG.error("预约报名课程失败", e);
            return ResponseBean.error("预约报名课程失败", e.getMessage());
        }
    }

    @ApiModelProperty(value = "获取当前用户参加的课程", notes = "获取当前用户所参见的课程")
    @PostMapping("userCourses")
    public ResponseBean getUserApplyCourse(@RequestBody PageFilterDTO pageFilterDTO) {
        try {
            Page<CourseSimple> result = courseService.getUserCourses(pageFilterDTO);
            return ResponseBean.ok("获取成功", result);
        } catch (Exception e) {
            LOG.error("获取当前用户参加的课程失败", e);
            return ResponseBean.error("获取失败", e.getMessage());
        }
    }
}
