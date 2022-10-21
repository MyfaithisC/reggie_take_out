package reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.pojo.Category;

import java.io.Serializable;

public interface CategoryService extends IService<Category> {
   void removeById(Long id);
}
