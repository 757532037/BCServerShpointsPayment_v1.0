package bc.core.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bc.core.utils.BCUtils;

public class Main {
	private static Log log = LogFactory.getLog(MsgHandler.class);
	private static ConfigurableApplicationContext    appContext;

	public synchronized static ConfigurableApplicationContext getAppContext() {
		return appContext;
	}

	public static void main(String[] args)
	{
		String date = BCUtils.getTime("yyyyMMdd");
		String time = BCUtils.getTime("HHmmss");
		if (args.length == 2 && args[0].equals("-init"))
		{

			//Properties prop = new Properties();
			try
			{
				appContext = new ClassPathXmlApplicationContext(args[1]);    
//				BCRecData app = (BCRecData) appContext.getBean("bc-db");
//				app.update9YAfterLimit("123456");
				 
				String a = "a";
				String b = a;
			
			} catch (Exception e)
			{
				log.error(e,e);
				System.out.println(e.getMessage());
			}

		} else {
			System.out.println("Argument syntax: -init propfile");
		}

	}



}