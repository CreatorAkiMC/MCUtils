package com.aki.mcutils.threads;

import com.aki.mcutils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import java.util.concurrent.*;

public class MCUtilsThreadManager {
    public static final ExecutorService FileFJExecutor = new ForkJoinPool(Math.max(Runtime.getRuntime().availableProcessors() - 2, 1),
            pool -> new ForkJoinWorkerThread(pool) {
            }, (thread, exception) -> Minecraft.getMinecraft().crashed(new CrashReport("MCUtilsThreadManager_FileFJExecutor", exception)), true);

    /**
     * Don't use it for your rendering engine
     * */
    public static final ThreadPoolExecutor executor;
    static {
        ThreadWorkQueue queue = new ThreadWorkQueue();
        executor = new ThreadPoolExecutor(0, Math.max(Runtime.getRuntime().availableProcessors() - 2, 1), 1L, TimeUnit.SECONDS, queue, queue);
    }

    public final Thread GenThread = Thread.currentThread();

    public static void Shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10_000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(10_000, TimeUnit.MILLISECONDS))
                    MCUtils.logger.error("MCUtilsThreadManager did not terminate!");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
