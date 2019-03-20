package org.flightythought.smile.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flightythought.smile.admin.bean.ResponseBean;
import org.flightythought.smile.admin.common.GlobalConstant;
import org.flightythought.smile.admin.database.entity.DiseaseClassDetailEntity;
import org.flightythought.smile.admin.database.entity.DiseaseClassEntity;
import org.flightythought.smile.admin.database.entity.SysUserEntity;
import org.flightythought.smile.admin.dto.DiseaseClassDetailDTO;
import org.flightythought.smile.admin.service.DiseaseDetailConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/diseaseDetail")
@Api(value = "疾病配置", tags = "疾病小类")
public class DiseaseDetailConfigController {

    private final DiseaseDetailConfigService diseaseDetailConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(DiseaseDetailConfigController.class);

    @Autowired
    public DiseaseDetailConfigController(DiseaseDetailConfigService diseaseDetailConfigService) {
        this.diseaseDetailConfigService = diseaseDetailConfigService;
    }

    @GetMapping("/majorClass")
    @ApiOperation(value = "获取疾病类目", notes = "获取疾病类目", position = 1)
    public ResponseBean getDiseaseClass() {
        List<DiseaseClassEntity> diseaseClassEntities = diseaseDetailConfigService.getDiseaseClass();
        return ResponseBean.ok("返回成功", diseaseClassEntities);
    }

    @GetMapping("/majorDetails")
    @ApiOperation(value = "获取疾病类目明细", notes = "获取疾病类目明细", position = 2)
    public ResponseBean getDiseaseDetail(int diseaseId, int pageNumber, int pageSize) {
        Page<DiseaseClassDetailEntity> result = diseaseDetailConfigService.getDiseaseDetails(diseaseId, pageNumber, pageSize);
        return ResponseBean.ok("返回成功", result);
    }

    @PostMapping("/saveDiseaseDetail")
    @ApiOperation(value = "新增疾病小类", notes = "新增疾病小类", position = 3)
    public ResponseBean saveDiseaseDetail(@RequestBody DiseaseClassDetailDTO diseaseClassDetailDTO, @ApiIgnore HttpSession session) {
        SysUserEntity sysUserEntity = (SysUserEntity) session.getAttribute(GlobalConstant.USER_SESSION);
        DiseaseClassDetailEntity result = diseaseDetailConfigService.saveDiseaseClassDetail(sysUserEntity, diseaseClassDetailDTO);
        return ResponseBean.ok("新增成功", result);
    }

    @PutMapping("/updateDiseaseDetail")
    @ApiOperation(value = "修改疾病小类", notes = "修改疾病小类", position = 4)
    public ResponseBean updateDiseaseDetail(@RequestBody DiseaseClassDetailDTO diseaseClassDetailDTO, @ApiIgnore HttpSession session) {
        SysUserEntity sysUserEntity = (SysUserEntity) session.getAttribute(GlobalConstant.USER_SESSION);
        DiseaseClassDetailEntity result = diseaseDetailConfigService.updateDiseaseClassDetail(sysUserEntity, diseaseClassDetailDTO);
        return ResponseBean.ok("修改成功", result);
    }

    @DeleteMapping("/deleteDiseaseDetail/{id}")
    @ApiOperation(value = "删除疾病小类", notes = "删除疾病小类", position = 5)
    public ResponseBean deleteDiseaseDetail(@PathVariable("id") Integer id) {
        diseaseDetailConfigService.deleteDiseaseDetail(id);
        return ResponseBean.ok("删除成功!");
    }
}