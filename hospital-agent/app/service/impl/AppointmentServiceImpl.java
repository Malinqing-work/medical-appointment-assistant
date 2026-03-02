package com.hospital.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.app.domain.Appointment;
import com.hospital.app.mapper.AppointmentMapper;
import com.hospital.app.service.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {
        @Override
        public boolean save(Appointment appointment) {
            return super.save(appointment);
        }
        @Override
        public Appointment getOne(String name, String idCard, String department, String doctor, String time) {
            LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //身份证号查询
            lambdaQueryWrapper.eq(Appointment::getIdCard, idCard);
            //科室名称查询
            lambdaQueryWrapper.eq(Appointment::getDepartment, department);
            //医生名称查询
            lambdaQueryWrapper.eq(Appointment::getDoctor, doctor);
            //患者名称查询
            lambdaQueryWrapper.eq(Appointment::getName, name);
            return getOne(lambdaQueryWrapper);
        }

        @Override
    public Appointment getOne(String name, String idCard, String time) {
            LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //身份证号查询
            lambdaQueryWrapper.eq(Appointment::getIdCard, idCard);
            //患者名称查询
            lambdaQueryWrapper.eq(Appointment::getName, name);
            //预约时间查询
            lambdaQueryWrapper.eq(Appointment::getTime, time);
            return getOne(lambdaQueryWrapper);
        }

        @Override
    public boolean remove(String name, String idCard, String time) {
            LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //身份证号查询
            lambdaQueryWrapper.eq(Appointment::getIdCard, idCard);
            //患者名称查询
            lambdaQueryWrapper.eq(Appointment::getName, name);
            //预约时间查询
            lambdaQueryWrapper.eq(Appointment::getTime, time);
            return remove(lambdaQueryWrapper);
        }
}
