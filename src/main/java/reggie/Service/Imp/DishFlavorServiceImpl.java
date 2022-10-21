package reggie.Service.Imp;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.Mapper.DishFlavorMapper;
import reggie.Service.DishFlavorService;
import reggie.pojo.DishFlavor;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
