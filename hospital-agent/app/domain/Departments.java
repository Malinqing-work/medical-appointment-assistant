package com.hospital.app.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 科室信息对象 departments
 * 
 * @author 王嘉豪
 * @date 2025-07-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departments implements Serializable
{
    @TableId
    private String departmentId;
    private String departmentName;
    private String departmentPhone;
    private String description;
    private String createdAt;
    private String updatedAt;
    @TableLogic
    private Integer deleted;
}
