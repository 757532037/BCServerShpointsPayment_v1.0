package bc.core.checkpoints;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
public class ShpointsTimeOutJob {
	private static final Logger logger = Logger.getLogger(ShpointsTimeOutJob.class);
	//冲正队列
	public final static ConcurrentLinkedQueue<ShpointsTimeOutJobBean> correctionQueue = new ConcurrentLinkedQueue<ShpointsTimeOutJobBean>();
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private final Thread nitifyThread;
	/**冲正循环次数*/
	private Integer frequency;
	/**冲正间隔时间*/
	private Integer waitTime;
	
	public ShpointsTimeOutJob(){
		nitifyThread = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("------------------ShpointsTimeOutJob main thread start!");
				while (true) {
					if (correctionQueue.size() > 0) {
						if (correctionQueue.peek().getTradeTime() < System.currentTimeMillis()) {
							ShpointsTimeOutJobBean bean = correctionQueue.poll();
							bean.setFrequency(frequency == null ? 3 : frequency);
							bean.setWaitTime(waitTime == null ? 5 : waitTime);
							threadPool.execute(bean);
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						logger.error("ShpointsTimeOutJob main thread sleep error!");
						logger.error(e, e);
					}
				}
			}
		});
		nitifyThread.start();
	}
	/**冲正循环次数*/
	public Integer getFrequency() {
		return frequency;
	}
	/**冲正循环次数*/
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/**冲正间隔时间*/
	public Integer getWaitTime() {
		return waitTime;
	}
	/**冲正间隔时间*/
	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}
	
	
}
