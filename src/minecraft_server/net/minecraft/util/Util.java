package net.minecraft.util;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class Util
{
    @Nullable
    public static <V> V runTask(FutureTask<V> task, Logger logger)
    {
        try
        {
            task.run();
            return task.get();
        }
        catch (ExecutionException executionexception)
        {
            logger.fatal("Error executing task", (Throwable)executionexception);
        }
        catch (InterruptedException interruptedexception)
        {
            logger.fatal("Error executing task", (Throwable)interruptedexception);
        }

        return (V)null;
    }

    public static <T> T getLastElement(List<T> list)
    {
        return list.get(list.size() - 1);
    }
}
