package reggie.Service.Imp;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.Mapper.EmployeeMapper;
import reggie.Service.EmployeeService;
import reggie.pojo.Employee;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
