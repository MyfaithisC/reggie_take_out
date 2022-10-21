package reggie.Service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.Mapper.ShoppingCartMapper;
import reggie.Service.ShoppingCartService;
import reggie.pojo.ShoppingCart;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
