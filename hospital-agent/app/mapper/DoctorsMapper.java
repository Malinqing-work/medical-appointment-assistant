package com.hospital.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.app.domain.Doctors;
import com.hospital.app.domain.dto.DoctorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DoctorsMapper extends BaseMapper<Doctors> {
    List<DoctorDTO> list();
    List<DoctorDTO> listByDept(String departmentName);
}
