package com.hospital.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.hospital.app.domain.Schedules;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO extends Schedules {
    //医生名称
    private String doctorName;
}