package dev.jakub.terminal;

import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.live.ProgressBar;
import dev.jakub.terminal.live.ProgressBarBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProgressBarTest {

    @Test
    void updateChangesCurrentValue() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ProgressBar bar = new ProgressBar(100, 40, ProgressBar.Style.BLOCK, support, System.out);
        bar.update(60);
        assertEquals(60, bar.getCurrent());
        assertEquals(100, bar.getTotal());
    }

    @Test
    void updateClampsToTotal() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ProgressBar bar = new ProgressBar(100, 40, ProgressBar.Style.BLOCK, support, System.out);
        bar.update(200);
        assertEquals(100, bar.getCurrent());
    }

    @Test
    void incrementIncrementsByOne() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ProgressBar bar = new ProgressBar(100, 40, ProgressBar.Style.BLOCK, support, System.out);
        bar.update(10);
        bar.increment();
        assertEquals(11, bar.getCurrent());
    }

    @Test
    void builderBuildsWithCorrectTotalAndStyle() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ProgressBar bar = new ProgressBarBuilder(support)
                .total(50)
                .width(30)
                .style(ProgressBar.Style.HASH)
                .build();
        assertEquals(50, bar.getTotal());
        bar.update(25);
        assertEquals(25, bar.getCurrent());
    }

    @Test
    void outputContainsBarAndPercentage() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProgressBar bar = new ProgressBarBuilder(support)
                .total(100)
                .width(10)
                .output(new PrintStream(baos))
                .build();
        bar.update(50);
        bar.complete();
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("["));
        assertTrue(out.contains("]"));
        assertTrue(out.contains("%"));
    }

    @Test
    void concurrentUpdatesAreThreadSafe() throws InterruptedException {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProgressBar bar = new ProgressBarBuilder(support)
                .total(10_000)
                .output(new PrintStream(baos))
                .build();
        int threads = 4;
        int incrementsPerThread = 2_500;
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        for (int t = 0; t < threads; t++) {
            exec.submit(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                for (int i = 0; i < incrementsPerThread; i++) {
                    bar.increment();
                }
            });
        }
        start.countDown();
        exec.shutdown();
        assertTrue(exec.awaitTermination(30, TimeUnit.SECONDS));
        assertEquals(10_000, bar.getCurrent());
    }
}
