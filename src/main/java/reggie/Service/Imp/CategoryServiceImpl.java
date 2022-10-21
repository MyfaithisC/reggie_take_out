package reggie.Service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reggie.Mapper.CategoryMapper;
import reggie.Mapper.DishMapper;
import reggie.Mapper.SetmealMapper;
import reggie.Service.CategoryService;
import reggie.Service.DishService;
import reggie.Service.SetmealService;
import reggie.common.CustomException;
import reggie.pojo.Category;
import reggie.pojo.Dish;
import reggie.pojo.Setmeal;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService SetmealService;
    @Autowired
    private  CategoryService categoryService;
    @Override
    public void removeById(Long id) {
        //判断菜品和套餐中是否存储分类的id号,如果不存在则删除
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",id);
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",id);
        int count = dishService.count(dishQueryWrapper);
        int count1 = SetmealService.count(setmealQueryWrapper);
        //判断是否关联了菜品
        if(count>0){
        //可以自定义异常
           throw  new CustomException("当前分类下关联了菜品,不能删除");
        }
        //判断是否关联了套餐
        if(count1>0){
         //可以自定义异常
            throw  new CustomException("当前分类下关联了套餐,不能删除");
        }
        //如果都没有关联,则可以删除
        else{
            categoryService.removeById(id);
        }

    }
}
