package me.ele.idgen.sample;
import java.util.ArrayList;
import java.util.List;

import me.ele.elog.Log;
import me.ele.elog.LogFactory;
import me.ele.idgen.client.MemIDPool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @Description: 简单的使用方式
 * @author chaney.chan
 * @date 2015年9月11日
 */
public class TestIdgen {
	
	private final static Log log  = LogFactory.getLog(TestIdgen.class);
	
	public final static Integer IDGENKEY_EFFECTIVE = 60*24*30*30;
	private final static String IDGENKEY = "idgen-key-" + "n10" + "-12";
	public static void main(String[] args) throws Exception{
		final String runJarVersion = System.getProperty("runjar.version");
		String elogHome = System.getProperty("elog.home");
		String elog =System.getProperty("elog.config");
		final String numStr = System.getProperty("num");
		log.info("runjar:" +runJarVersion);
		log.info("elogHome:" +elogHome);
		log.info("elog:" +elog);
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:client.xml");
		final MemIDPool idPool = context.getBean(MemIDPool.class);
		//RedisClientProxy redisClientProxy = context.getBean(RedisClientProxy.class);
		Thread.sleep(3000);
	    long start =System.currentTimeMillis();
		long times = 0;
		new Thread(){
			@Override
			public void run() {
				for(int i = 0 ; i< Integer.parseInt(numStr);i++){
					List<String> params = new ArrayList<String>();
					String msg = "";
					String id ="null";
					try {
						//
						id = i+"" ;// 
						id = idPool.borrow();
					} catch (Exception e) {
						msg = e.getMessage();
					}finally{
						//redisClientProxy.incr(IDGENKEY,IDGENKEY_EFFECTIVE);
						params.add(id);
						params.add(msg);
						params.add(runJarVersion);
						DBToolkit.insertIdgen(params);
					}
				}
				System.out.println(idPool.getFreshIds());
				System.out.println("version:"+runJarVersion
						+", leave nums："+ idPool.getFreshIds().size());
			};
			
		}.start();
		
		//redisClientProxy.set(IDGENKEY, times, IDGENKEY_EFFECTIVE);
		//log.error("--总共执行的次数--" + times);
		//log.error("----执行-----" +(System.currentTimeMillis() - start)); 
		//System.exit(0);
	}
}
