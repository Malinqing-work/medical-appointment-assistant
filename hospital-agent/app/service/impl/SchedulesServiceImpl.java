package com.hospital.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.app.domain.Schedules;
import com.hospital.app.domain.dto.ScheduleDTO;
import com.hospital.app.mapper.SchedulesMapper;
import com.hospital.app.service.ISchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班管理Service业务层处理
 * 
 * @author 王嘉豪
 * @date 2025-07-10
 */
@Service
public class SchedulesServiceImpl extends ServiceImpl<SchedulesMapper, Schedules> implements ISchedulesService
{
    @Autowired
    private SchedulesMapper schedulesMapper;
    @Override
    public List<ScheduleDTO> list(Schedules schedules){
        return schedulesMapper.list();
    }
    @Override
    public List<ScheduleDTO> listByDoctor(String doctorName){
        return schedulesMapper.listByDoctor(doctorName);
    }
}