package com.hospital.app.tools;

import com.hospital.app.domain.Appointment;
import com.hospital.app.domain.Departments;
import com.hospital.app.domain.dto.DoctorDTO;
import com.hospital.app.domain.dto.ScheduleDTO;
import com.hospital.app.mapper.DoctorsMapper;
import com.hospital.app.mapper.SchedulesMapper;
import com.hospital.app.service.AppointmentService;
import com.hospital.app.service.DepartmentsService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HospitalTools {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DepartmentsService departmentsService;
    @Autowired
    private DoctorsMapper doctorsMapper;
    @Autowired
    private SchedulesMapper schedulesMapper;

    @Tool(name = "预约挂号", value = "该方法进行挂号预约，需输入患者名称、身份证、医生和科室信息")
    public String appointment(@P("患者名称") String name, @P("患者身份证") String idCard,
                              @P("科室名称") String department,
                              @P("医生名称") String doctor,
                              @P("预约时间") String time) {
        Appointment appointment = appointmentService.getOne(name, idCard, department, doctor, time);
        if (appointment == null) {
            Appointment a=new Appointment();
            a.setName(name);
            a.setIdCard(idCard);
            a.setDepartment(department);
            a.setDoctor(doctor);
            a.setTime(time);
            if (appointmentService.save(a)) {
                return "预约成功";
            }
            return "预约失败";
        }
        return "该患者已成功预约";
    }

    @Tool(name = "取消预约", value = "取消预约需输入患者身份证号码")
    public String cancelAppointment(@P("身份证号") String idCard,
                                    @P("患者姓名") String name,
                                    @P("预约时间") String time) {
        Appointment appointment = appointmentService.getOne(name, idCard, time);
        if (appointment != null) {
            if (appointmentService.remove(name, idCard, time)) {
                return "取消预约成功";
            }
            return "取消失败";
        }
        return "没有预约信息,请核对预约信息";
    }
    @Tool(name = "查询科室信息", value = "查询所有科室名称")
    public List<Departments> queryDepartment() {
        return departmentsService.list();
    }
    @Tool(name = "查询医生信息", value = "查询所有医生信息")
    public List<DoctorDTO> queryDoctor() {
        List<DoctorDTO> list =  doctorsMapper.list();
        return list;
    }
    @Tool(name = "查询医生排班信息", value = "查询某个医生排班信息")
    public List<ScheduleDTO> querySchedule(@P("医生名称") String doctorName) {
        List<ScheduleDTO> list =  schedulesMapper.listByDoctor(doctorName);
        return list;
    }
    @Tool(name = "查询科室医生信息", value = "查询某个科室的医生信息")
    public List<DoctorDTO> queryDepartmentDoctor(@P("科室名称") String departmentName) {
        List<DoctorDTO> list =  doctorsMapper.listByDept(departmentName);
        return list;
    }
    @Tool(name = "查询今天日期", value = "查询今天日期")
    public static LocalDate getTodayDate() {
        return LocalDate.now();
    }
}