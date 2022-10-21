package reggie.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reggie.Service.ShoppingCartService;
import reggie.pojo.ShoppingCart;
import java.util.*;
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 将添加的菜品或者套餐添加到购物车中
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public  R<ShoppingCart> addShopToCat(@RequestBody ShoppingCart shoppingCart){
        //设置用户id,指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart1 = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCart,shoppingCart1);
        shoppingCart1.setUserId(currentId);
        //根据菜品id或者套餐id查询是否在购物车中
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        //确定是否为同一个用户的操作
        shoppingCartQueryWrapper
                .eq("user_id",currentId)
                .eq(shoppingCart.getDishId()!=null,"dish_id",shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,"setmeal_id",shoppingCart.getSetmealId());

        ShoppingCart one = shoppingCartService.getOne(shoppingCartQueryWrapper);
        if(one!=null){
            Integer number = one.getNumber();
            Integer  updateNumber=number+1;
            one.setNumber(updateNumber);
            shoppingCartService.update(one,shoppingCartQueryWrapper);
            return R.success(one);
        }
        else{
            shoppingCart1.setNumber(1);
            shoppingCartService.save(shoppingCart1);
            return  R.success(one);
        }
    }

    /**
     * 将添加的菜品或者套餐显示到购物车中
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>>  showShoppingCart(){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        shoppingCartQueryWrapper.eq("user_id",currentId)
                .orderByAsc("create_time");
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartQueryWrapper);
        return R.success(list);
    }

    /**
     * 清空当前用户的购物车
     * @return
     */
    @DeleteMapping("/clean")
    public  R<String> removeShoppingCart(){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getCurrentId());
        shoppingCartService.remove(shoppingCartQueryWrapper);
        return R.success("清空购物车成功");
    }
}
