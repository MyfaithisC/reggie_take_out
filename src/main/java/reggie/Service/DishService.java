package reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.dto.DishDto;
import reggie.pojo.Dish;

public interface DishService extends IService<Dish> {
    //联合添加操作
    void addDishAndDishFlavor(DishDto dishDto);
    //联合查询操作
     DishDto  selectDishAndDisFlvaor(Long id);
    //进行联合更新操作
    void updateDishAndDisFlavaor(DishDto dishDto);
}
