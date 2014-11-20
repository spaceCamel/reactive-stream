package spacecamel.reactivestream;

import org.junit.Test;
import rx.Scheduler;
import rx.schedulers.TestScheduler;

public class ExperimentOnTestScheduler
{
    final TestScheduler scheduler = new TestScheduler();

    final Scheduler.Worker worker = scheduler.createWorker();

    @Test
    public void run()
    {
        worker.schedule(() -> System.out.println("A"));
        worker.schedule(() -> System.out.println("B"));
        scheduler.triggerActions();
        System.out.println("FINISHED");
    }

    @Test
    public void withoutTriggeringActions()
    {
        worker.schedule(() -> System.out.println("A"));
        worker.schedule(() -> System.out.println("B"));
        //        scheduler.triggerActions();
        System.out.println("FINISHED");
    }
}
