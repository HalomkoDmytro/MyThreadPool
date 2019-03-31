import gl.App;
import gl.exceptions.WorkQueueIsFullException;
import gl.executor.MyExecutorService;
import gl.executor.MyExecutors;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class MyExecutorsTest {

    private Runnable getTask() {
        return () -> {
            double a = new Random().nextInt(10000);
            for (int i = 0; i < 1_000_0000; i++) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("is interrupt");
                    return;
                }
                a = a + Math.tan(a);
            }
            System.out.println("Task is done.");
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
