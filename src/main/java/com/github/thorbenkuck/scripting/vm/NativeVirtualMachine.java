package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.keller.sync.Synchronize;
import com.github.thorbenkuck.scripting.Register;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

final class NativeVirtualMachine implements VirtualMachine {

	private final Register register = Register.create();
	private final InstructionSet instructionSet;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Object containerLock = new Object();
	private final Synchronize synchronize = Synchronize.createDefault();
	private final BlockingDeque<Task> taskQueue = new LinkedBlockingDeque<>();
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private Thread container;

	NativeVirtualMachine(InstructionSet instructionSet) {
		this.instructionSet = instructionSet;
	}

	@Override
	public synchronized void start() {
		if (running.get()) {
			return;
		}

		Thread thread = new Thread(this);
		thread.setName("JAF-VM");
		thread.setPriority(8);

		thread.start();
		try {
			synchronize.synchronize();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public synchronized void stop() {
		if (!running.get()) {
			return;
		}

		synchronize.reset();
		running.set(false);
		container.interrupt();
	}

	@Override
	public void run() {
		synchronized (containerLock) {
			container = Thread.currentThread();
		}

		running.set(true);
		synchronize.goOn();

		while (running.get() && !Thread.interrupted()) {
			try {
				Task task = taskQueue.take();
				threadPool.execute(task);
			} catch (InterruptedException ignored) {
			}
		}

		List<Runnable> runnables = threadPool.shutdownNow();
		if (!runnables.isEmpty()) {
			System.err.println(runnables.size() + " tasks not finished");
		}
	}

	@Override
	public void execute(ByteCode byteCode) {
		taskQueue.addLast(new Task(byteCode));
	}

	private final class Task implements Runnable {

		private final Pointer pointer = Pointer.create();
		private final ByteCode byteCode;

		private Task(ByteCode byteCode) {
			this.byteCode = byteCode;
		}

		@Override
		public void run() {
			final Interpreter interpreter = new Interpreter(register, pointer, instructionSet);

			// Maybe change this. Maybe create a wrapper
			// class, that allows for changes within the
			// byte code. This would mean, we could write
			// self modifying code.
			final byte[] data = byteCode.toByteCode();
			long start = System.currentTimeMillis();
			while (pointer.get() < data.length) {

				final byte current = data[pointer.get()];
				pointer.increase();
				if (interpreter.prepare(current)) {
					interpreter.execute();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println(end - start);
		}
	}
}
