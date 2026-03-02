package com.hospital.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedules implements Serializable
{
    @TableId(type = IdType.AUTO)
    private Long scheduleId;
    private Long doctorId;
    private String workday;
    private String workTime;
    private Long remain;
    @TableLogic
    private Integer deleted;
    private String createdAt;
    private String updatedAt;
}
