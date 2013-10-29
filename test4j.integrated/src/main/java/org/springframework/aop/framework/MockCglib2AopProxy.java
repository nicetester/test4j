package org.springframework.aop.framework;

import mockit.Mock;
import mockit.MockUp;
import net.sf.cglib.proxy.Enhancer;

/**
 * 多次加载spring容器，回到classloader中的classes内存溢出<br>
 * 原因是spring aop使用cglib代理是，Enhancer的cache会hold住很多spring代理类的class信息<br>
 * 导致GC无法释放，最终导致OOM
 * 
 * @author darui.wudr
 */
//@MockClass(realClass = Cglib2AopProxy.class, instantiation = Instantiation.PerMockSetup)
public class MockCglib2AopProxy extends MockUp<Cglib2AopProxy> {
    @Mock
    public Enhancer createEnhancer() {
        Enhancer enhancer = new Enhancer();
        enhancer.setUseCache(false);
        return enhancer;
    }
}
