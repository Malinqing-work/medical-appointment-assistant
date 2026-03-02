package hospital.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hospital.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AppointmentMapper  extends BaseMapper<Appointment> {
}
