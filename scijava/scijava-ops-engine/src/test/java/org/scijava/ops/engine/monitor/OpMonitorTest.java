package org.scijava.ops.engine.monitor;

import java.math.BigInteger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.scijava.ops.api.OpBuilder;
import org.scijava.ops.engine.AbstractTestEnvironment;
import org.scijava.ops.spi.Op;
import org.scijava.ops.spi.OpClass;
import org.scijava.types.Nil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests correct functionality of {@link OpMonitor}
 * 
 * @author Marcel Wiedenmann
 * @author Gabriel Selzer
 *
 */
public class OpMonitorTest extends AbstractTestEnvironment {

	@BeforeAll
	public static void addNeededOps() {
		ops.register(new InfiniteOp());
		ops.register(new CountingOp());
	}

	/**
	 * Basic test of cancellation on the same thread
	 */
	@Test
	public void testCancellation() {
		Function<OpMonitor, BigInteger> bigOp = OpBuilder.matchFunction(ops, "test.opMonitor", new Nil<OpMonitor>() {},
				new Nil<BigInteger>() {});
		OpMonitor monitor = new DefaultOpMonitor();
		monitor.cancel();
		// NOTE: we have to cancel the op before we call it because the execution of
		// this Op and the test that call it are on the same thread.
		assertThrows(CancellationException.class, () -> bigOp.apply(monitor));
	}

	/**
	 * Tests cancellation over different threads
	 * 
	 * @throws InterruptedException
	 *             - should not happen
	 */
	@Test
	public void testCancellationDifferentThread() throws InterruptedException {
		Function<OpMonitor, BigInteger> bigOp = OpBuilder.matchFunction(ops, "test.opMonitor", new Nil<OpMonitor>() {},
				new Nil<BigInteger>() {});
		OpMonitor monitor = new DefaultOpMonitor();
		try {
			Future<BigInteger> future = Executors.newSingleThreadExecutor().submit(() -> bigOp.apply(monitor));
			monitor.cancel();
			future.get();
			fail();
		} catch (ExecutionException exc) {
			Throwable cancellationException = exc.getCause();
			Assertions.assertTrue(cancellationException instanceof CancellationException);
		}
	}
	
	@Test
	public void testProgress() throws InterruptedException, ExecutionException{
		BiFunction<OpMonitor, BigInteger, BigInteger> bigOp = OpBuilder.matchFunction(ops, "test.progress", new Nil<OpMonitor>() {},
				new Nil<BigInteger>() {}, new Nil<BigInteger>() {});
		
		OpMonitor monitor = new DefaultOpMonitor();
		BigInteger target = BigInteger.valueOf(1000000);
		double progress = monitor.getProgress();
		assert progress == 0;
		Future<BigInteger> future = Executors.newSingleThreadExecutor().submit(() -> bigOp.apply(monitor, target));
		while(!future.isDone()){
			assert progress <= monitor.getProgress();
			progress = monitor.getProgress();
		}
		BigInteger bi = future.get();
		Assertions.assertTrue(bi.equals(target));
		Assertions.assertEquals(monitor.getProgress(), 1, 0);
	}

}

@OpClass(names = "test.opMonitor")
class InfiniteOp implements Function<OpMonitor, BigInteger>, Op {

	@Override
	public BigInteger apply(OpMonitor opMonitor) {
		BigInteger bi = BigInteger.valueOf(0);
		while (true) {
			opMonitor.checkCanceled();
			bi.add(BigInteger.ONE);
			// will never be true
			if (bi.compareTo(BigInteger.valueOf(-1)) < 0)
				break;
		}
		return bi;
	}

}

@OpClass(names = "test.progress")
class CountingOp implements BiFunction<OpMonitor, BigInteger, BigInteger>, Op {

	@Override
	public BigInteger apply(OpMonitor opMonitor, BigInteger target) {
		BigInteger bi = BigInteger.valueOf(0);
		while(target.compareTo(bi) > 0) {
			bi = bi.add(BigInteger.ONE);
			opMonitor.setProgress(bi.doubleValue() / target.doubleValue());
		}
		return bi;
	}
	
}
