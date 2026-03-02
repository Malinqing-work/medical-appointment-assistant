package com.hospital.app.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctors implements Serializable {
    @TableId
    private String doctorId;
    private String departmentId;
    private String doctorName;
    private String doctorPhone;
    private String gender;

}
