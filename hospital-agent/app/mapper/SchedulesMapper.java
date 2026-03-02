package com.hospital.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.app.domain.Schedules;
import com.hospital.app.domain.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SchedulesMapper extends BaseMapper<Schedules>
{
    List<ScheduleDTO> list();
    List<ScheduleDTO> listByDoctor(String doctorName);
}