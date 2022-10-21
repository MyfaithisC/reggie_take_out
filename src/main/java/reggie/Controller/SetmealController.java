package reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reggie.Service.CategoryService;
import reggie.Service.SetmealService;
import reggie.common.R;
import reggie.dto.SetmealDto;
import reggie.pojo.Category;
import reggie.pojo.Setmeal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询套餐所以信息(链表查询)
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> findSetmealByPage(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<Setmeal>(page, pageSize);
        Page<SetmealDto> SetmealDtoPage = new Page<>();
        QueryWrapper<Setmeal> SetmealQueryWrapper = new QueryWrapper<>();
        SetmealQueryWrapper.likeRight(!StringUtils.isEmpty(name), "name", name);
        setmealService.page(pageInfo, SetmealQueryWrapper);
        //对象的拷贝,拷贝除了records的记录,其他的都拷贝(因为records包含了数据)
        BeanUtils.copyProperties(pageInfo, SetmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();//获取分类id
            //根据分类的id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) { //防止菜品Dish的id值在Category菜品分类中找不到报空指针异常
                //获取分类名字
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        SetmealDtoPage.setRecords(list);
        return R.success(SetmealDtoPage);
    }

    /**
     * 保存套餐信息以及套餐与菜品之间的联系
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "Setmal",allEntries = true)
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.addSetmealAndSetmealDish(setmealDto);
        return R.success("套餐信息保存成功");
    }

    /**
     * 查询套餐和套餐与菜品之间的关系回显到更新的页面上
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> showSetmealforUpadate(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.showSetmealAndDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐与菜品之间的关系
     *
     * @param setmealDto
     * @return
     */
    @CacheEvict("Setmal")
    @PutMapping
    public R<String> updateToSeteamlAndSeteamlDish(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmealAndDish(setmealDto);
        return R.success("更新套餐成功");
    }

    /**
     * 修改套餐的出售的状态
     * @param status
     * @param ids
     * @return
     */
    /**
     * 当更新套餐数据时,自动清除缓存中的数据
     * @param status
     * @param ids
     * @return
     */
    @CacheEvict(value = "Setmal",allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> stopStatus(@PathVariable int status, Long ids) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(ids);
        setmealService.updateById(setmeal);
        return R.success("更改状态成功");
    }

    /**
     * 删除套餐和套餐与菜品之间的联系信息
     * @param ids
     * @return
     */
    /**
     * 当删除套餐数据时,自动清除缓存中的数据
     * @param ids
     * @return
     */
    @CacheEvict(value = "Setmal",allEntries = true)
    @DeleteMapping
    public R<String> deleteSeteam(Long[] ids) {
      setmealService.deleteSetmealAndSetmealAndDish(ids);
      return  R.success("套餐删除成功");
    }

    /**
     * 根据条件查询并显示到移动端上
     * @param categoryId
     * @param status
     * @return
     */
    /**
     * 加入缓存操作(利用了spring-Cache),
     * Cacheable:先查缓存,缓存中没有时,再查数据库,再将数据库中的数据
     * 存入缓存中
     * @param categoryId
     * @param status
     * @return
     */
    @Cacheable(value = "Setmal",key = "#categoryId+'_'+#status")
    @GetMapping("/list")
    public R<List<Setmeal>> listSemealAndDish(Long categoryId,Integer status){
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.eq(categoryId!=null,"category_Id",categoryId)
                .eq(status!=null,"status",status);
        List<Setmeal> list = setmealService.list(setmealQueryWrapper);
        return R.success(list);
    }
}
