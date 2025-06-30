// File: TestFile.java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFile {

    @Test
    @Order(1)
    public void testSameInstance() {
        Logger l1 = Logger.getInstance();
        Logger l2 = Logger.getInstance();
        assertSame(l1, l2, "Logger instances are not the same (Singleton failed)");
    }

    @Test
    @Order(2)
    public void testLogOutput() {
        Logger logger = Logger.getInstance();
        assertDoesNotThrow(() -> logger.log("Another message"));
    }

    @Test
    @Order(3)
    public void testPrivateConstructor() {
        try {
            var constructor = Logger.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor is not private");
        } catch (Exception e) {
            fail("Constructor is missing or inaccessible");
        }
    }

    @Test
    @Order(4)
    public void testThreadSafeSingleton() throws InterruptedException, ExecutionException {
        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<Logger>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(executor.submit(Logger::getInstance));
        }

        Set<Logger> instances = new HashSet<>();
        for (Future<Logger> future : futures) {
            instances.add(future.get());
        }

        executor.shutdown();
        assertEquals(1, instances.size(), "Logger is not thread-safe (multiple instances created)");
    }
}
