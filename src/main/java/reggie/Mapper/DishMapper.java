package reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import reggie.pojo.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
