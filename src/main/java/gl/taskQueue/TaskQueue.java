package gl.taskQueue;

import java.util.Objects;

public class TaskQueue {

    private volatile Node tail;
    private volatile int size;

    public TaskQueue() {
    }

    public synchronized void add(Runnable runnable) {
        Objects.requireNonNull(runnable);
        Node node = new Node(runnable);
        node.next = tail;
        tail = node;
        size++;
    }

    public synchronized Runnable poll() {
        if (size == 0) {
            return null;
        }
        Node<Runnable> node = tail;
        tail = tail.next;
        size--;
        return node.item;
    }

    public int getSize() {
        return size;
    }

    private static class Node<Runnable> {
        private Node next;
        private Runnable item;

        Node(Runnable item) {
            this.item = item;
        }
    }
}
