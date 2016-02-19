package me.ele.idgen.sample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.ele.idgen.client.MemIDPool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client.xml");
		MemIDPool idPool = context.getBean(MemIDPool.class);
//		Thread.sleep(2000);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch countDownLatch = new CountDownLatch(10);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			executorService.execute(new PoolThread(idPool, countDownLatch));
		}
		countDownLatch.await();
		long end = System.currentTimeMillis();
		System.out.println("-------------------------"+(end - start)+"--------------------------------");

	}

	private static class PoolThread implements Runnable {

		private MemIDPool idPool;
		private CountDownLatch countDownLatch;

		PoolThread(MemIDPool idPool, CountDownLatch latch) {
			this.idPool = idPool;
			this.countDownLatch = latch;
		}

		public void run() {
			for (int i = 0; i < 100000; i++) {
				try {
					String id = idPool.borrow();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			countDownLatch.countDown();
		}

	}
	
}
