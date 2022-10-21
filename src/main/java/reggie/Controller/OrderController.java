package reggie.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reggie.Service.OrderService;
import reggie.common.R;
import reggie.pojo.Orders;
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public Page<Orders> ShowOrderiphone(Integer page,Integer pageSize){
        Page<Orders> ordersPage = new Page<Orders>(page,pageSize);
         orderService.page(ordersPage);
         return   ordersPage;
    }
    @GetMapping("/page")
    public Page<Orders> ShowOrderForPC(Integer page,Integer pageSize){
        Page<Orders> ordersPage = new Page<Orders>(page,pageSize);
        orderService.page(ordersPage);
        return   ordersPage;
    }
}
