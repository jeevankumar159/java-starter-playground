import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.util.concurrent.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFile {

    @Test @Order(1)
    public void testSingletonSameInstance() throws Exception {
        Class<?> loggerClass = Class.forName("Logger");
        Method getInstance = loggerClass.getMethod("getInstance");
        Object instance1 = getInstance.invoke(null);
        Object instance2 = getInstance.invoke(null);
        assertSame(instance1, instance2, "Logger should return the same instance");
    }

    @Test @Order(2)
    public void testThreadSafeSingleton() throws Exception {
        Class<?> loggerClass = Class.forName("Logger");
        Method getInstance = loggerClass.getMethod("getInstance");

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<Object> task = () -> getInstance.invoke(null);
        Object[] instances = new Object[10];
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.submit(() -> {
                instances[index] = task.call();
            });
        }
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        Object first = instances[0];
        for (int i = 1; i < 10; i++) {
            assertSame(first, instances[i], "All instances should be the same in multithreaded env");
        }
    }

    @Test @Order(3)
    public void testLoggerHasLogMethod() throws Exception {
        Class<?> loggerClass = Class.forName("Logger");
        Method logMethod = loggerClass.getMethod("log", String.class);
        assertNotNull(logMethod, "Logger class should have a log(String) method");
    }
}
