package com.hospital.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * #创建挂号表
 * create table appointment(
 *     id varchar(40) primary key not null comment '主键',
 *     name varchar(20) comment '姓名',
 *     idcard varchar(20) comment '身份证号',
 *     department varchar(20) comment '科室',
 *     doctor varchar(20) comment '医生',
 *     time varchar(20) comment '预约时间, 上午、下午'
 * )
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String idCard;
    private String department;
    private String doctor;
    private String time;
}
