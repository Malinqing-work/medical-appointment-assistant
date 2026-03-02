package hospital.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hospital.dao.AppointmentMapper;
import hospital.entity.Appointment;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 数学计算
 */
@Component
public class HospitalTools {
    @Autowired
    private AppointmentService appointmentService;

    @Tool(name = "预约挂号", value = "该方法进行挂号预约，必须输入患者名称、身份证、医生和科室信息")
    public String appointment(@P("患者名称") String name,
                              @P("患者身份证") String idcard,
                              @P("科室名称") String department,
                              @P("医生名称") String doctor,
                              @P("预约时间") String time) {
        LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //身份证号查询
        lambdaQueryWrapper.eq(Appointment::getIdcard, idcard);
        //科室名称查询
        lambdaQueryWrapper.eq(Appointment::getDepartment, department);
        //医生名称查询
        lambdaQueryWrapper.eq(Appointment::getDoctor, doctor);
        //患者名称查询
        lambdaQueryWrapper.eq(Appointment::getName, name);
        Appointment appointment = appointmentService.getOne(lambdaQueryWrapper);
        if (appointment == null) {
            Appointment a=new Appointment(UUID.randomUUID().toString(), name, idcard, department, doctor, time);
            if (appointmentService.save(a)) {
                return "预约成功";
            }
            return "预约失败";
        }
        return "该患者已预约";

    }

    @Tool(name = "取消预约", value = "取消预约输入患者身份证号码")
    public String cancelAppointment(@P("患者份证号") String idCard ) {

        LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //身份证号查询
        lambdaQueryWrapper.eq(Appointment::getIdcard, idCard);
        Appointment appointment = appointmentService.getOne(lambdaQueryWrapper);
        if (appointment != null) {
            if (appointmentService.remove(lambdaQueryWrapper)) {
                return "取消预约成功";
            }
            return "取消失败";
        }
        return "没有预约信息请先预约";

    }


}
