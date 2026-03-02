package com.hospital.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.app.domain.Departments;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepartmentsMapper extends BaseMapper<Departments> {
    List<Departments> list();
}
