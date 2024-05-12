import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FIFOQueue {
    private BlockingQueue<String> queue;

    public FIFOQueue(int maxSize) {
        queue = new LinkedBlockingQueue<>(maxSize);
    }

    public void put(String item) throws InterruptedException {
        queue.put(item);
    }

    public String take() throws InterruptedException {
        return queue.take();
    }
}