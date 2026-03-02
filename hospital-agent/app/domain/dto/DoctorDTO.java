package com.hospital.app.domain.dto;

import com.hospital.app.domain.Doctors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO extends Doctors {
    private String departmentName;
}