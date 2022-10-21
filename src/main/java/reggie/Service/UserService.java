package reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.pojo.User;

public interface UserService extends IService<User> {
    //发送邮件
    void sendMsg(String to,String subject,String text);
}
