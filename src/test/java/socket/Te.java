package socket;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Te {
    String config = "appcontext/applicationContext.xml";
    ApplicationContext ac = new ClassPathXmlApplicationContext(config);
    @Test
    public void test(){
        LXUnitService unitService = ac.getBean(LXUnitService.class);
        LXUnitBean unit = new LXUnitBean();
        for (int i = 1; i < 400; i++) {
            unit.setUnit_num((short) i);
            unitService.createDataTable(unit);
        }
    }
}
