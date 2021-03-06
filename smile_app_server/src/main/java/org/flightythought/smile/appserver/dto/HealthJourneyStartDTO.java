package org.flightythought.smile.appserver.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Copyright 2019 Flighty-Thought All rights reserved.
 *
 * @Author: LiLei
 * @ClassName HealthJourneyStartDTO
 * @CreateTime 2019/4/9 16:34
 * @Description: TODO
 */
@Data
@ApiModel(value = "开启养生旅程DTO", description = "开启养生旅程DTO")
public class HealthJourneyStartDTO {

    @ApiModelProperty(value = "养生旅程ID")
    private Integer journeyId;

    @ApiModelProperty(value = "体检指标类型")
    private List<HealthNormTypeDTO> healthNormTypes;

    @ApiModelProperty(value = "养生旅程名称")
    private String journeyName;

    @ApiModelProperty(value = "概述")
    private String summarize;

    @ApiModelProperty(value = "体检报告")
    private List<FileImageDTO> files;

    @ApiModelProperty(value = "选择的疾病小类ID集合")
    private List<Integer> diseaseClassDetailIds;

    @ApiModelProperty(value = "封面图片ID")
    private Integer coverImageId;

}
