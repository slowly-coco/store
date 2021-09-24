import com.bjpowernode.service.ProductInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MyTest {
    @Autowired
    ProductInfoService productInfoService;
    @Test
        public void testSelectCondition(){
        productInfoService.delete(1);
    }
}
