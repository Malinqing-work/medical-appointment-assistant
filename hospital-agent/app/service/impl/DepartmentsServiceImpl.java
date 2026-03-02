package com.hospital.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.app.domain.Departments;
import com.hospital.app.mapper.DepartmentsMapper;
import com.hospital.app.service.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentsServiceImpl extends ServiceImpl<DepartmentsMapper, Departments> implements DepartmentsService {
    @Autowired
    private DepartmentsMapper departmentsMapper;
    @Override
    public List<Departments> list(){
        return departmentsMapper.list();
    }
}
