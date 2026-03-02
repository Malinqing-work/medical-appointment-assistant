package com.hospital.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.app.domain.Appointment;

public interface AppointmentService extends IService<Appointment> {
    boolean save(Appointment appointment);
    Appointment getOne(String name, String idCard, String department, String doctor, String time);
    Appointment getOne(String name, String idCard, String time);
    boolean remove(String name, String idCard, String time);
}
