package reggie.fliter;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import reggie.common.BaseContext;
import reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 实现登陆拦截器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class loginCheckFilter implements Filter {
    //用于路径匹配
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的URI
        String requestURI = request.getRequestURI();

        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg", //移动端发送短信
                "/user/login" //移动端登入
        };
        boolean check1 = check(uris, requestURI);
        if (check1) {
            log.info("check方法被调用,放行");
            log.info("拦截的请求 {}",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute(("employee")) != null) {
            //存储用户id的值
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute(("user")) != null) {
            //存储用户id的值
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute(("employee")) == null) {
            //如果未登录，通过输出流方式向客户端响应数据
            response.getWriter().write(JSON.toJSONString(R.error( "NOTLOGIN")));
            log.error("未登录,拦截页面 {}",requestURI);
        }
    }
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestURI);
            if (match){
                return  true;
            }
        }
        return false;
    }
}