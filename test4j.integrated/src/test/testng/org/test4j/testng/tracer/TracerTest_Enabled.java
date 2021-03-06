package org.test4j.testng.tracer;

import java.io.FileNotFoundException;

import mockit.Mock;

import org.test4j.database.table.ITable;
import org.test4j.fortest.hibernate.User;
import org.test4j.fortest.hibernate.UserService;
import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.annotations.SpringBeanByType;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.tracer.TracerHelper;
import org.test4j.testng.Test4J;
import org.test4j.tools.commons.ResourceHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project.xml" })
@Test(groups = { "test4j", "tracer", "broken-install", "hibernate" })
public class TracerTest_Enabled extends Test4J implements IDatabase {
    @SpringBeanByType
    private UserService userService;

    @BeforeMethod
    public void disabledTracer() {
        new MockUp<TracerHelper>() {
            @Mock
            public boolean doesTracerEnabled() {
                return true;
            }
        };
    }

    @Test
    public void monitorSpringTrue() {
        db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
            {
                this.put("id", "1", "2", "3");
                this.put("is_deleted", "false", "false", "true");
            }
        });

        User user1 = userService.getUser(1);
        want.object(user1).notNull();

        User user = new User();
        user.setName("new user");
        userService.newUser(user);
        db.table(ITable.t_tdd_user).queryWhere("name='new user'").reflectionEqMap(1, new DataMap() {
            {
                this.put("count", "1");
            }
        });

    }

    @Test(dependsOnMethods = "monitorSpringTrue")
    public void monitorSpringTrue_check() throws FileNotFoundException {
        String tracerInfo = ResourceHelper
                .readFromFile("target/tracer/org/test4j/tracer/TracerTest#monitorSpringTrue.html");
        want.string(tracerInfo).contains("call").contains("paras").contains("result");
    }

    @Test
    public void monitorJdbcTrue() {
        db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
            {
                this.put("id", "1", "2", "3");
                this.put("is_deleted", "false", "false", "true");
            }
        });
        User user1 = userService.getUser(1);
        want.object(user1).notNull();

        User user = new User();
        user.setName("new user");
        userService.newUser(user);
        db.table(ITable.t_tdd_user).queryWhere("name='new user'").reflectionEqMap(1, new DataMap() {
            {
                this.put("count", "1");
            }
        });
    }

    @Test(dependsOnMethods = "monitorSpringTrue")
    public void monitorJdbcTrue_check() throws FileNotFoundException {
        String tracerInfo = ResourceHelper
                .readFromFile("target/tracer/org/test4j/tracer/TracerTest#monitorJdbcTrue.html");
        want.string(tracerInfo).contains("SQL-Statement");
    }

    @Test
    public void monitorSpring_ToInfoString() {
        db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
            {
                this.put("id", "1", "2", "3");
                this.put("is_deleted", "false", "false", "true");
            }
        });
        User user1 = userService.getUser(1);
        want.object(user1).notNull();

        User user = new User();
        user.setName("new user");
        userService.newUser(user);
        db.table(ITable.t_tdd_user).queryWhere("name='new user'").reflectionEqMap(1, new DataMap() {
            {
                this.put("count", "1");
            }
        });
    }

    @Test(dependsOnMethods = "monitorSpring_ToInfoString")
    public void monitorSpring_ToInfoString_check() throws FileNotFoundException {
        String tracerInfo = ResourceHelper
                .readFromFile("target/tracer/org/test4j/tracer/TracerTest#monitorSpring_ToInfoString.html");
        want.string(tracerInfo).contains("org.test4j.fortest.hibernate.User@");
    }
}
