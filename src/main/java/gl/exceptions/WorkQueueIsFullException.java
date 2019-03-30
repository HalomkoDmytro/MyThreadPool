package gl.exceptions;

public class WorkQueueIsFullException extends RuntimeException {
    public WorkQueueIsFullException() {
        super();
    }

    public WorkQueueIsFullException(String message) {
        super(message);
    }
}
