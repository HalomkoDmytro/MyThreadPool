package gl.executor;

import gl.exceptions.WorkQueueIsFullException;
import gl.taskQueue.TaskQueue;

public class MyExecutors implements MyExecutorService {

    private final PullWorker[] threads;
    private final TaskQueue queue;
    private final int workQueueSize;

    private MyExecutors(int poolSize, int workQueueSize) {
        queue = new TaskQueue();
        threads = new PullWorker[poolSize];

        for (int i = 0; i < poolSize; i++) {
            threads[i] = new PullWorker();
            threads[i].start();
        }

        this.workQueueSize = workQueueSize;
    }

    private class PullWorker extends Thread {
        @Override
        public void run() {
            Runnable task = queue.poll();
            while (task != null) {
                task.run();
                task = queue.poll();
                while (!isInterrupted() && task == null) {
                    task = queue.poll();
                }
            }
        }
    }

    /**
     * Creates a new ThreadPool with the given initial number of threads and work queue size
     *
     * @param poolSize      the number of threads to keep in the pool, even
     *                      if they are idle
     * @param workQueueSize the queue to use for holding tasks before they are
     *                      executed.  This queue will hold only the {@code Runnable}
     *                      tasks submitted by the {@code execute} method.
     */
    public static MyExecutorService newFixedThreadPool(int poolSize, int workQueueSize) {
        return new MyExecutors(poolSize, workQueueSize);
    }

    public static MyExecutorService newFixedThreadPool(int poolSize) {
        return newFixedThreadPool(poolSize, 10);
    }

    @Override
    public void execute(Runnable command) {
        synchronized (queue) {
            if (queue.getSize() >= workQueueSize) {
                throw new WorkQueueIsFullException("Work queue is full, max size of queue are " + workQueueSize);
            }
            queue.add(command);
        }
    }

    @Override
    public void shutdownNow() {
        for (PullWorker pullWorker : threads) {
            pullWorker.interrupt();
        }
    }
}
