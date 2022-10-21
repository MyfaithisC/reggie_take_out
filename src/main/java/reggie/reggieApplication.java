package reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
//扫描Servlet上的注解
@ServletComponentScan
@EnableTransactionManagement
//开启缓存
@EnableCaching
public class reggieApplication {
    public static void main(String[] args) {
            SpringApplication.run(reggieApplication.class,args);
                    log.info("项目启动成功....");

    }
}
