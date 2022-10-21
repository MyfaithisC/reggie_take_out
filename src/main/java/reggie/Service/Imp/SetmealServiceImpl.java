package reggie.Service.Imp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reggie.Mapper.SetmealMapper;
import reggie.Service.SetmealDishService;
import reggie.Service.SetmealService;
import reggie.common.CustomException;
import reggie.dto.SetmealDto;
import reggie.pojo.Setmeal;
import reggie.pojo.SetmealDish;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐信息以及套餐与菜品之间的联系
     * @param setmealDto
     */
    @Override
    public void addSetmealAndSetmealDish(SetmealDto setmealDto) {
            //保存套餐表的信息
            this.save(setmealDto);
            //保存套餐与菜品的联系信息表
          List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
          for(int i=0;i<setmealDishes.size();i++){
                 setmealDishes.get(i).setSetmealId(setmealDto.getId());
          }
          setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐和套餐与菜品之间的联系并回显到页面上
     * @param id
     * @return
     */
    @Override
    public SetmealDto showSetmealAndDish(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",id);
        List<SetmealDish> list = setmealDishService.list(setmealDishQueryWrapper);
        setmealDto.setSetmealDishes(list);
         return  setmealDto;
    }

    /**
     *根据修改的信息更新套菜表和套餐与菜品之间的类型并提交
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateSetmealAndDish(SetmealDto setmealDto) {
          //更新套餐表
          this.updateById(setmealDto);
          //更新套菜和菜品之间的关系
          List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
           Long  Id = setmealDto.getId();
          //先将套餐与菜品之间的关系信息删除,然后再添加
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",Id);
        setmealDishService.remove(setmealDishQueryWrapper);
          for(SetmealDish setmealDish:setmealDishes){
              setmealDish.setSetmealId(Id);
              setmealDishService.save(setmealDish);

          }
    }

    /**
     * 联表删除套餐表和套餐与菜品的联系表
     * @param ids
     */
    @Override
    @Transactional
    public void deleteSetmealAndSetmealAndDish(Long[] ids) {
        //查询是否可以删除
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.in("id",ids)
                        .eq("status",1);
        int count = this.count(setmealQueryWrapper);
        if(count>0){
            throw new CustomException("商品正在售卖中,不能删除...");
        }
        else{
            //如果可以删除,先删除套餐表中的数据
            for(Long id:ids){
                this.removeById(id);
                System.out.println(id);
                //再删除和套餐菜品关联表中的数据
                QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
                setmealDishQueryWrapper.eq("setmeal_id",id);
                setmealDishService.remove(setmealDishQueryWrapper);
            }

        }
    }
}

