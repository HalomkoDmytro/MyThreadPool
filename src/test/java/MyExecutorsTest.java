import gl.exceptions.WorkQueueIsFullException;
import gl.executor.MyExecutorService;
import gl.executor.MyExecutors;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class MyExecutorsTest {

    private Runnable getTask() {
        return () -> {
            double a = 999;
            for (int i = 0; i < 1_000_000; i++) {
                a = a + Math.tan(a);
            }
            System.out.println("task complete!");
        };
    }

    @Test(expected = WorkQueueIsFullException.class)
    public void testToMuchWork() {
        MyExecutorService executor = MyExecutors.newFixedThreadPool(2, 2);
        executor.execute(getTask());
        executor.execute(getTask());
        executor.execute(getTask());
    }

    @Test
    public void expectProprietyClass() {
        assertTrue(MyExecutorService.class.isAssignableFrom(MyExecutors.newFixedThreadPool(1).getClass()));
        assertTrue(MyExecutorService.class.isAssignableFrom(MyExecutors.newFixedThreadPool(1, 1).getClass()));
    }

}
