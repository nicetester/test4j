package org.test4j.module.spring.strategy.register;

import org.junit.Test;
import org.test4j.fortest.service.UserService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

/**
 * 验证多个表达符合接口类型时获取实现
 * 
 * @author darui.wudr
 */
@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.impl.*DaoImpl"), /** <br> */
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest_MultiPattern extends Test4J {
    @SpringBeanByName
    private UserService userService;

    @Test
    public void checkUserAnotherDao_NotFound() {
        Object userDao = spring.getBean("userAnotherDao");
        want.object(userDao).notNull();
    }

    @Test
    public void checkUserServiceBean() {
        want.object(userService).notNull();
        Object o = spring.getBean("userService");
        want.object(o).same(userService);
    }
}
