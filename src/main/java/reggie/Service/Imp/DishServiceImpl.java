package reggie.Service.Imp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reggie.Mapper.DishMapper;
import reggie.Service.CategoryService;
import reggie.Service.DishFlavorService;
import reggie.Service.DishService;
import reggie.dto.DishDto;
import reggie.pojo.Dish;
import reggie.pojo.DishFlavor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ASUS
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 保存菜品的基本信息同时插入口味信息
     * @param dishDto
     */
    @Override
    @Transactional  //开启事务
    public void addDishAndDishFlavor(DishDto dishDto) {

         //保存菜品的基本信息
         this.save(dishDto);
         //获取并保存Dish ID的值到DishFlavor中
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品的信息和菜品口味
     * @param id
     */
    @Override
    public DishDto selectDishAndDisFlvaor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return  dishDto;
    }

    /**
     * 对回显的数据进行修改并提交
     * @param dishDto
     */
    @Override
    @Transactional  //开启事务保证数据的一致性
    public void updateDishAndDisFlavaor(DishDto dishDto) {
        //1.更新Dish菜品表
        this.updateById(dishDto);
        //2.删除原来的DishFlavor表中的口味数据
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id",dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);
        //3.获取并保存Dish ID的值到DishFlavor中(否则会插入失败)
        //4.插入新的DisFlavor的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
