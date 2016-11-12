package me.ele.idgen.sample;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import me.ele.idgen.client.MemIDPool;

/**
 * 当业务方每次通过borrow方法拿到id的时候，idgen并不知道业务方是否最终使用了此id，所以会存在如下两种情况：
 * <li>正常情况下，业务方borrow一个id，并且用于自己的业务中，插入数据库。
 * <li>异常情况下，业务方borrow一个id之后，自己的业务出错，这个时候id可能并没有用插入数据库，为了不浪费id建议程序调用giveback()方法，将id还回来以备下次继续使用。
 * @Description: 最佳实践
 * @author chaney.chan
 * @date 2015年9月11日
 */
public class BestPractice {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client.xml");
		MemIDPool idPool = context.getBean(MemIDPool.class);
		String id = "";
		boolean success = true;
		try {
			id = idPool.borrow();
			// TODO do someting
		} catch (Throwable t) {
			success = false;
		} finally {
			try {
				if (success) {
					if (id != null) {
						idPool.consume(id);
					}
				} else {
					if (id != null) {
						idPool.giveback(id);
					}
				}
			} catch (Throwable t) {
				System.out.println(String.format("failed to return/consume id from id pool! id=%s, sucess=%s", id, success));
			}
		}
	}
}
