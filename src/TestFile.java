// src/TestFile.java
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.*;

public class TestFile {

    @Test
    public void testSameInstance() {
        Logger l1 = Logger.getInstance();
        Logger l2 = Logger.getInstance();
        assertSame(l1, l2);
    }

    @Test
    public void testLogOutput() {
        Logger logger = Logger.getInstance();
        logger.log("Testing log output");
        // No exception = pass
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        var constructor = Logger.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testThreadSafeSingleton() throws Exception {
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
        assertEquals(1, instances.size());
    }
}
