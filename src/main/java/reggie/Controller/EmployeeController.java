package reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reggie.Service.EmployeeService;
import reggie.common.R;
import reggie.pojo.Employee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static reggie.common.R.success;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    //登入功能
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
         //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp= employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("登录失败");
        }
        //4.密码比对,如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //5.查看员工状态,如果已经为禁用状态,则返回员工已经禁用结果
        if(emp.getStatus()==0){
            return  R.error("该员工为禁用状态");
        }
        //6.登入成功,将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return success(emp);
    }
    //退出功能
    @PostMapping("/logout")
    public  R<String>  logout(HttpServletRequest request){
      request.getSession().removeAttribute("employee");
      return  success("退出成功");
    }
    //添加员工
    @PostMapping
    public R<String> save(HttpSession session, @RequestBody Employee employee){
        log.info("employee={}"+employee);
        String password="123456";
        //对密码进行md5加密
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取session域中的userId
        Long empId =(Long) session.getAttribute("employee");
        //标注是谁创建和修改了这个员工
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return success("新增员工成功");
    }
    //分页展示员工信息
    @GetMapping("/page")
    public  R<Page> pageShow(Integer page,Integer pageSize,String name){
        //查询所有员工

        //进行分页查询
        Page<Employee> employeePage = new Page<Employee>(page,pageSize);
        //根据分页查询的数据进行过滤查询
        QueryWrapper<Employee> queryWrapper = new QueryWrapper();
        queryWrapper.like(!StringUtils.isEmpty(name),"name",name);
        //添加排序条件
        queryWrapper.orderByDesc("update_time");
        employeeService.page(employeePage,queryWrapper);
        return success(employeePage);
    }
    //修改员工的状态信息
    @PutMapping
    public R<String> updateEmp(HttpServletRequest request,@RequestBody Employee employee){
//        Long empid=(long)request.getSession().getAttribute("employee");
       //标注是谁修改了这个员工
//      employee.setUpdateUser(empid);
        employeeService.updateById(employee);
       return  R.success("更新成功");
    };
    @GetMapping("/{id}")
    public R<Employee> updateEMP(@PathVariable Long id){
        log.info("查询用户信息");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return  R.success(employee);
        }
    else{
        return  R.error("没有查询到该员工信息");
        }
    }
}
