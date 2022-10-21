package reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reggie.Service.CategoryService;
import reggie.Service.DishFlavorService;
import reggie.Service.DishService;
import reggie.common.R;
import reggie.dto.DishDto;
import reggie.pojo.Category;
import reggie.pojo.Dish;
import reggie.pojo.DishFlavor;

import  java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class disController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 查询所有的菜品(链表查询)
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> selectDish(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.orderByDesc("price")
                .likeRight(!StringUtils.isEmpty(name), "name", name);
        dishService.page(pageInfo, dishQueryWrapper);
        //对象的拷贝,拷贝除了records的记录,其他的都拷贝(因为records包含了数据)
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        //可测试
//        System.out.println("================records=============="+
//                records);
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//获取分类id
            //根据分类的id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) { //防止菜品Dish的id值在Category菜品分类中找不到报空指针异常
                //获取分类名字
                String categoryName = category.getName();

                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 添加菜品并保存少量的菜品口味信息
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        dishService.addDishAndDishFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 将菜品信息回显到页面的数据并携带菜品口味
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> updateForDish(@PathVariable Long id) {
        DishDto dishDto = dishService.selectDishAndDisFlvaor(id);
        return R.success(dishDto);
    }

    /**
     * 进行对菜品信息更新操作
     *
     * @return
     */
    @PutMapping
    public R<String> updateReally(@RequestBody DishDto dishDto) {
        dishService.updateDishAndDisFlavaor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 将查询出来的菜品显示到套餐中
     *
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> showDishForSetmeal(Dish dish) {
//        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
//        dishQueryWrapper.eq(!StringUtils.isEmpty(dish.getCategoryId()), "category_id", dish.getCategoryId())
//                .eq("status", 1); //获取状态为1的套餐(状态1:为启售)
//        List<Dish> list = dishService.list(dishQueryWrapper);
//        if (list != null) {
//            return R.success(list);
//        } else {
//            return R.error("没有查询到该套餐的菜品分类");
//        }
//    }

    /**
     *对查询出来的菜品以及对应的菜品口味显示到移动端上
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
