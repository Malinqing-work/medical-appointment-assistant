package com.hospital.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.app.domain.Departments;

import java.util.List;

public interface DepartmentsService extends IService<Departments> {
    List<Departments> list();
}
