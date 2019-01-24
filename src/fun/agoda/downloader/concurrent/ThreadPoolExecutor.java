package fun.agoda.downloader.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutor {

    final private ExecutorService executorService;

    public ThreadPoolExecutor(final int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void registerNewTask(final Runnable runnableTask) {
        executorService.submit(runnableTask);
    }

    public void waitToExecuteAllTasks() {
        executorService.shutdown();
    }
}
