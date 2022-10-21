package reggie.Service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.Mapper.OrderDetailMapper;
import reggie.Service.OrderDetailService;
import reggie.pojo.OrderDetail;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}