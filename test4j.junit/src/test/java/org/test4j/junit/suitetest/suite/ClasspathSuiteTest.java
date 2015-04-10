package org.test4j.junit.suitetest.suite;

import static org.junit.Assert.assertSame;
import static org.test4j.junit.filter.SuiteType.JUNIT38_TEST_CLASSES;
import static org.test4j.junit.filter.SuiteType.JUNT4_TEST_CLASSES;
import static org.test4j.junit.filter.SuiteType.SUITE_TEST_CLASSES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mockit.Mock;
import mockit.Mocked;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.annotations.ClazFinder.BaseType;
import org.test4j.junit.filter.ClasspathFilterFactory;
import org.test4j.junit.filter.SuiteType;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;
import org.test4j.module.ICore;

@SuppressWarnings("unchecked")
public class ClassPathSuiteTest implements ICore {

    private TestClazFinder finder;

    @Mocked({ "runners" })
    private RunnerBuilder  builder;

    @RunWith(ClassPathSuite.class)
    private static class PlainSuite {
    }

    @Test
    public void findWithDefaultValues() throws InitializationError {
        finder = new MockUp<TestClazFinder>() {
            @Mock
            public List<Class<?>> find() {
                return Collections.EMPTY_LIST;
            }
        }.getMockInstance();
        new MockUp<ClasspathFilterFactory>() {
            @Mock(invocations = 1)
            public TestClazFinder create(String classpathProperty, FilterCondiction testerFilter) {
                want.string(classpathProperty).isEqualTo("java.class.path");
                want.object(testerFilter).reflectionEq(
                        new FilterCondiction(false, new String[0], new SuiteType[] { JUNT4_TEST_CLASSES },
                                new Class<?>[] { Object.class }, new Class<?>[0]));
                return finder;
            }
        };
        new ClassPathSuite(PlainSuite.class, builder);
    }

    @RunWith(ClassPathSuite.class)
    @ClazFinder(patterns = { "filter1", "!filter2" }, inJars = true, value = { SUITE_TEST_CLASSES, JUNIT38_TEST_CLASSES }, baseType = @BaseType(includes = ClassPathSuiteTest.class, excludes = { ClassPathSuite.class }))
    private static class ComplexSuite {
    }

    @Test
    public void findWithComplexValues() throws InitializationError {
        finder = new MockUp<TestClazFinder>() {
            @Mock
            public List<Class<?>> find() {
                return Collections.EMPTY_LIST;
            }
        }.getMockInstance();
        new MockUp<ClasspathFilterFactory>() {
            @Mock(invocations = 1)
            public TestClazFinder create(String classpathProperty, FilterCondiction testerFilter) {
                want.string(classpathProperty).isEqualTo("my.class.path");
                want.object(testerFilter).reflectionEq(
                        new FilterCondiction(true, new String[] { "filter1", "!filter2" }, new SuiteType[] {
                                SUITE_TEST_CLASSES, JUNIT38_TEST_CLASSES },
                                new Class<?>[] { ClassPathSuiteTest.class }, new Class<?>[] { ClassPathSuite.class }));
                return finder;
            }
        };
        new ClassPathSuite(ComplexSuite.class, builder);
    }

    private static class Test1 {
    }

    private static class Test2 {
    }

    @Test
    public void sortReturnedClassesByName() throws InitializationError {
        final List<Class<?>> listOfClasses = new ArrayList<Class<?>>();
        listOfClasses.add(Test2.class);
        listOfClasses.add(Test1.class);
        finder = new MockUp<TestClazFinder>() {
            @Mock
            public List<Class<?>> find() {
                return listOfClasses;
            }
        }.getMockInstance();
        new MockUp<ClasspathFilterFactory>() {
            @Mock(invocations = 1)
            public TestClazFinder create(String classpathProperty, FilterCondiction testerFilter) {
                return finder;
            }
        };
        new ClassPathSuite(PlainSuite.class, builder);
        assertSame(Test1.class, listOfClasses.get(0));
        assertSame(Test2.class, listOfClasses.get(1));
    }

    //    @RunWith(ClasspathSuite.class)
    //    public static class SuiteWithBefore {
    //        @BeforeSuite
    //        public static void before() {
    //            beforeWasRun = true;
    //        }
    //    }

    //    private static boolean beforeWasRun;

    //    @Test
    //    public void beforeSuite(RunNotifier notifier) throws InitializationError {
    //        beforeWasRun = false;
    //        ClasspathSuite cpsuite = new ClasspathSuite(SuiteWithBefore.class, builder);
    //        cpsuite.run(notifier);
    //        want.bool(beforeWasRun).is(true);
    //    }
}
