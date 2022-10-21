package reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import reggie.pojo.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}