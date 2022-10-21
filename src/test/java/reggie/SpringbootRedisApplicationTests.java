package reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import reggie.utils.RedisUtil;

import java.util.Set;

@SpringBootTest
public class SpringbootRedisApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheManager cacheManager;
    @Test
    void contextLoads() {}
    @Test
    public  void test(){
        redisUtil.set("k1","张启松");
        String s = redisUtil.get("k1");
        System.out.println(s);
        redisUtil.set("k2","小淦");
        String s1 = redisUtil.get("k2");
        System.out.println(s1);
        redisUtil.sAdd("s1","小淦","小名","小老弟");
        redisUtil.sAdd("s2","张启松","孙中伟","德才","小淦");
        //求并集
        Set<String> strings = redisUtil.sUnion("s1", "s2");
        //求交集
        Set<String> strings1 = redisUtil.sIntersect("s1", "s2");

        System.out.println(strings);
        System.out.println(strings1);


    }
}
