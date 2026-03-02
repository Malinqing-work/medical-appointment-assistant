package com.hospital.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.app.domain.Schedules;
import com.hospital.app.domain.dto.ScheduleDTO;

import java.util.List;

/**
 * 排班管理Service接口
 * 
 * @author 王嘉豪
 * @date 2025-07-10
 */
public interface ISchedulesService extends IService<Schedules>
{
    List<ScheduleDTO> list(Schedules schedules);
    List<ScheduleDTO> listByDoctor(String doctorName);
}
