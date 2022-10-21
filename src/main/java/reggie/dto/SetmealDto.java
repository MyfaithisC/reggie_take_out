package reggie.dto;

import lombok.Data;
import reggie.pojo.Dish;
import reggie.pojo.DishFlavor;
import reggie.pojo.Setmeal;
import reggie.pojo.SetmealDish;

import java.util.*;
@Data
public class SetmealDto extends Setmeal {
   //菜品分类名称
   private  String categoryName;
   //菜品和套餐关系的集合
   private List<SetmealDish>  setmealDishes;
}
