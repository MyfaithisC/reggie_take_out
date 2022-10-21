package reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reggie.Service.CategoryService;
import reggie.common.BaseContext;
import reggie.common.R;
import reggie.pojo.Category;
import java.util.*;
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 分页展示所有的菜品分类
     */
    @GetMapping("/page")
    public R<Page> showCategory(Integer page,Integer pageSize){
        Page<Category> categoryPage = new Page<Category>(page,pageSize);
        QueryWrapper<Category> querywrapper = new QueryWrapper<>();
        querywrapper.orderByDesc("sort");
        categoryService.page(categoryPage,querywrapper);
        System.out.println(BaseContext.getCurrentId());
        return  R.success(categoryPage);
    }

    /**
     * 修改菜品分类信息
     * @param category
     * @return
     */
    @PutMapping
    public  R<String> updateFood(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 删除菜品分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteFood(Long id){
        //自定义删除方法
        categoryService.removeById(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 添加菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public  R<String> addFood(@RequestBody Category category){
        categoryService.save(category);
        return  R.success("新增分析信息成功");
    }

    /**
     * 将查询出来的分类显示到移动端上
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> showCategoryToDish(Category category){
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq(!StringUtils.isEmpty(category.getType()),"type",category.getType())
                .orderByDesc("sort")
               .orderByAsc("update_time");
        List<Category> list = categoryService.list(categoryQueryWrapper);
        return  R.success(list);

    }
}
