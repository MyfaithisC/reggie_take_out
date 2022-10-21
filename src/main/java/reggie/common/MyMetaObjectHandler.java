package reggie.common;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //进行插入策略
    @Override
    public void insertFill(MetaObject metaObject) {
    this.setFieldValByName("createTime",LocalDateTime.now(),metaObject);
    this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
    this.setFieldValByName("createUser",BaseContext.getCurrentId(),metaObject);
    this.setFieldValByName("orderTime",LocalDateTime.now(),metaObject);
    this.setFieldValByName("checkoutTime",LocalDateTime.now(),metaObject);

    }
    //进行更新策略
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        Long currentId = BaseContext.getCurrentId();
        System.out.println(currentId);
        this.setFieldValByName("updateUser",BaseContext.getCurrentId(),metaObject);
    }
}
