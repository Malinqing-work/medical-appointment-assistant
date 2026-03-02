package hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hospital.entity.Appointment;

public interface AppointmentService  extends IService<Appointment> {


    boolean save(Appointment appointment);



}
