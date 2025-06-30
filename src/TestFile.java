import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestFile {

    // IO Streams
    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private static ByteArrayInputStream inputStream;

    // Backup Streams
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;
    private static final InputStream originalInput = System.in;

    @BeforeAll
    public static void setStreams() {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalInput);
    }

    @Test
    @Order(1)
    public void testPrintOutput() {
        try {
            Class<?> classNameToTest = Class.forName("<Class Name Here>");
            Method methodToTest = classNameToTest.getMethod("<Method Name Here>");
            methodToTest.invoke(null, null);

            String actualOutput = out.toString().trim();
            assertEquals("Hello World", actualOutput); // replace expected value as needed

        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception.");
        }
    }
}
