package spacecamel.reactivestream;

import org.junit.Test;
import rx.Scheduler;
import rx.schedulers.TestScheduler;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class ExperimentOnTestSchedulerTest
{
    final TestScheduler scheduler = new TestScheduler();

    final Scheduler.Worker worker = scheduler.createWorker();

    final List<String> events = newArrayList();

    @Test
    public void actionsSubmittedWithNoDelayHappenAtTimeZeroInTheScheduledOrder()
    {
        worker.schedule(() -> events.add("A"));
        worker.schedule(() -> events.add("B"));
        assertThat(events).isEmpty();
        scheduler.triggerActions();
        assertThat(events).isEqualTo(list("A", "B"));
    }

    @Test
    public void actionsAddedWithDelayHappenAtGivenTime()
    {
        worker.schedule(() -> events.add("A"), 2, MILLISECONDS);
        worker.schedule(() -> events.add("B"), 3, MILLISECONDS);
        worker.schedule(() -> events.add("C"), 1, MILLISECONDS);
        assertThat(events).isEmpty();
        scheduler.advanceTimeTo(1, MILLISECONDS);
        assertThat(events).isEqualTo(list("C"));
        scheduler.advanceTimeTo(2, MILLISECONDS);
        assertThat(events).isEqualTo(list("C", "A"));
        scheduler.advanceTimeTo(3, MILLISECONDS);
        assertThat(events).isEqualTo(list("C", "A", "B"));
    }

    @Test
    public void actionsWithNoDelayHappenBeforeActionsWithDelay()
    {
        worker.schedule(() -> events.add("A"), 3, MILLISECONDS);
        worker.schedule(() -> events.add("B")); // enquivalent to ..., 0, TimeUnit.ANY);
        assertThat(events).isEmpty();
        scheduler.advanceTimeTo(3, MILLISECONDS);
        assertThat(events).isEqualTo(list("B", "A"));
    }

    @Test
    public void actionsWithNoDelayHappenBeforeActionsWithDelay_bis()
    {
        worker.schedule(() -> events.add("A"), 3, MILLISECONDS);
        worker.schedule(() -> events.add("B")); // enquivalent to ..., 0, TimeUnit.ANY);
        assertThat(events).isEmpty();
        scheduler.triggerActions();
        assertThat(events).isEqualTo(list("B"));
        scheduler.advanceTimeTo(3, MILLISECONDS);
        assertThat(events).isEqualTo(list("B", "A"));
    }

    private ArrayList<String> list(final String... elements)
    {
        return elements == null ? newArrayList() : newArrayList(elements);
    }
}
