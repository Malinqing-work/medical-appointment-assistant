package com.hospital.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.app.domain.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
