package hospital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * create table appointment
 * (
 *     id         varchar(40) primary key not null comment '主键',
 *     name       varchar(20) comment '姓名',
 *     idcard      varchar(20) comment '身份证号',
 *     department varchar(20) comment '科室',
 *     doctor     varchar(20) comment '医生',
 *     time       varchar(20) comment '预约时间,上午、下午'
 * )
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    // 主键
    private String id;
    // 姓名
    private String name;
    // 身份证号码
    private String idcard;
    // 科室
    private String department;
    // 医生
    private String doctor;
    // 预约时间,上午、下午
    private String time;
}
