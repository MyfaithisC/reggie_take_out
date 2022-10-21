package reggie.dto;

import lombok.Data;
import reggie.pojo.Dish;
import reggie.pojo.DishFlavor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO,用于展示层与服务层之间的数据传输
 * 因为请求体中的字段不匹配问题
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();
    //菜品名称
    private String categoryName;

    private Integer copies;
}
