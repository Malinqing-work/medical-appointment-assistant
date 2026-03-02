package com.hospital.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.app.domain.Doctors;
import com.hospital.app.domain.dto.DoctorDTO;

import java.util.List;

/**
 * 医生管理Service接口
 * 
 * @author 王嘉豪
 * @date 2025-07-09
 */
public interface IDoctorsService extends IService<Doctors>
{
    List<DoctorDTO> list(Doctors  doctors);
}
