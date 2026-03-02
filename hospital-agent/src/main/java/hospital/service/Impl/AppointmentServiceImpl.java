package hospital.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hospital.dao.AppointmentMapper;
import hospital.entity.Appointment;
import hospital.service.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl  extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Override
    public boolean save(Appointment appointment) {
        return super.save(appointment);
    }
}
