package org.test4j.json.helper;

import java.util.Map;

import org.junit.Test;
import org.test4j.json.JSON;
import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.DataFrom;

public class JSONSingleTest extends Test4J {

    @Test
    public void testToClazzName() {
        JSONSingle bean = new JSONSingle();
        bean.setValue("com.test4j.User@12345");
        String clazzname = bean.toClazzName();
        want.string(clazzname).isEqualTo("com.test4j.User");
    }

    @Test
    public void testToClazzName_NoRef() {
        JSONSingle bean = new JSONSingle();
        bean.setValue("com.test4j.User");
        String clazzname = bean.toClazzName();
        want.string(clazzname).isEqualTo("com.test4j.User");
    }

    @Test
    public void testToReferenceID() {
        JSONSingle bean = new JSONSingle();
        bean.setValue("com.test4j.User@12345");
        String clazzname = bean.toReferenceID();
        want.string(clazzname).isEqualTo("@12345");
    }

    @Test
    public void testToReferenceID_NoRef() {
        JSONSingle bean = new JSONSingle();
        bean.setValue("com.test4j.User");
        String clazzname = bean.toReferenceID();
        want.string(clazzname).isNull();
    }

    @Test
    @DataFrom("dataForToPrimitiveValue")
    public void testToPrimitiveValue(String input, Object expected) {
        Map<String, Object> actual = JSON.toObject(input);
        want.object(actual).propertyEq("key", expected);
    }

    public static DataIterator dataForToPrimitiveValue() {
        return new DataIterator() {
            {
                data("{key:null}", null);
                data("{key:1}", 1);
                data("{key:1L}", 1L);
                data("{key:0.7}", 0.7);
                data("{key:0.7f}", 0.7F);
                data("{key:0.7D}", 0.7D);
                data("{key:1xx}", "1xx");
                data("{key:xxxx}", "xxxx");
            }
        };
    }

    @Test
    public void testToPrimitiveValue_Array() {
        String json = "[1,2L,3.5,4D,true,false]";
        Object[] actual = JSON.toObject(json, Object[].class);
        want.array(actual).reflectionEq(new Object[] { 1, 2L, 3.5, 4, true, false });
    }
}
