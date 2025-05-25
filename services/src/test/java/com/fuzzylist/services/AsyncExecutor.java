package com.fuzzylist.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A helper class to execute tasks asynchronously.
 *
 * @author Guy Raz Nir
 * @since 2025/05/20
 */
public class AsyncExecutor {

    /**
     * Thread pool.
     */
    private final ExecutorService threadPoolExecutor;

    /**
     * Default number of threads in the thread pool.
     */
    private static final int DEFAULT_NUMBER_OF_THREADS = 10;

    /**
     * Class constructor.
     *
     * @param numberOfThreads Number of threads in the thread pool.
     */
    private AsyncExecutor(int numberOfThreads) {
        threadPoolExecutor = Executors.newFixedThreadPool(numberOfThreads);
    }

    /**
     * Create a new instance of {@link AsyncExecutor} with the given number of threads.
     *
     * @param numberOfThreads Number of threads in the thread pool.
     * @return New executor.
     */
    public static AsyncExecutor newExecutor(int numberOfThreads) {
        return new AsyncExecutor(numberOfThreads);
    }

    /**
     * Create a new instance of {@link AsyncExecutor} with the default number of threads.
     *
     * @return New executor.
     */
    public static AsyncExecutor newDefaultExecutor() {
        return newExecutor(DEFAULT_NUMBER_OF_THREADS);
    }

    /**
     * <p>Execute a runnable task asynchronously.
     * </p>
     * Upon completion, the returned {@link CompletableFuture} will either return {@code null} value via its
     * {@link Future#get()} method or will throw an exception the task has generated exception.
     *
     * @param runnable Runnable to execute.
     * @return A future that can be used to wait for the task to complete.
     * Upon successful completion, the future's {@code get} call will return {@code null}.
     * If the task fails, the future will be completed with an exception.
     */
    public Future<?> executeAsync(Runnable runnable) {
        final CompletableFuture<?> future = new CompletableFuture<>();

        threadPoolExecutor.execute(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });

        return future;
    }


}
