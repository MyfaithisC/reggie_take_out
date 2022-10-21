package reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.dto.SetmealDto;
import reggie.pojo.Setmeal;
import java.util.*;
public interface SetmealService extends IService<Setmeal> {
      /**
       * 联表添加操作
       * @param setmealDto
       */
      void addSetmealAndSetmealDish(SetmealDto setmealDto);

      /**
       * 联表查询套餐表和套餐与菜品之间的类型表
       * @param id
       * @return
       */
      SetmealDto showSetmealAndDish(Long id);

      /**
       * 联表更新套餐表和套餐表之间的信息
       * @param setmealDto
       */
      void updateSetmealAndDish(SetmealDto setmealDto);

      /**
       * 联表删除套餐表和套餐与菜品的联系表
       * @param id
       */
      void  deleteSetmealAndSetmealAndDish(Long[] id);

}
