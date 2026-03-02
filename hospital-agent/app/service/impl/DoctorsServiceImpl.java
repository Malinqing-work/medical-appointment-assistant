package com.hospital.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.app.domain.Doctors;
import com.hospital.app.domain.dto.DoctorDTO;
import com.hospital.app.mapper.DoctorsMapper;
import com.hospital.app.service.IDoctorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorsServiceImpl extends ServiceImpl<DoctorsMapper, Doctors> implements IDoctorsService
{
    @Autowired
    private DoctorsMapper doctorsMapper;

    @Override
    public List<DoctorDTO> list(Doctors doctors){
//        LambdaQueryWrapper<Doctors> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(StringUtils.isNotEmpty(doctors.getDoctorName()), Doctors::getDoctorName,doctors.getDoctorName());
//        return list(lambdaQueryWrapper);
        return doctorsMapper.list();
    }
}
