package me.ele.idgen.sample;

import java.util.concurrent.TimeUnit;

import me.ele.idgen.client.MemIDPool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @Description: 简单的使用方式
 * @author chaney.chan
 * @date 2015年9月11日
 */
public class Practice {
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client.xml");
		MemIDPool idPool = context.getBean(MemIDPool.class);
		TimeUnit.SECONDS.sleep(5);
		while (true) {
			TimeUnit.SECONDS.sleep(1);
			try {
				String id = idPool.borrow();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		// TODO do something
	}
}
