package com.feeyo.redis.engine.manage.stat;

import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class NetFlowCollector implements StatCollector {
	

	private static ConcurrentHashMap<String, UserNetFlow> userNetFlowMap = new ConcurrentHashMap<String, UserNetFlow>();


	@Override
	public void onCollect(String password, String cmd, String key, int requestSize, int responseSize, int procTimeMills,
			boolean isCommandOnly) {
		
		UserNetFlow userNetIo = userNetFlowMap.get(password);
		if ( userNetIo == null ) {
			userNetIo = new UserNetFlow();
			userNetIo.password = password;
			userNetFlowMap.put(password, userNetIo);
		}
		userNetIo.netIn.addAndGet(requestSize);
		userNetIo.netOut.addAndGet(responseSize);
	}

	@Override
	public void onScheduleToZore() {
		userNetFlowMap.clear();
	}

	@Override
	public void onSchedulePeroid(int peroid) {
	}
	
	public Set<Entry<String, UserNetFlow>> getUserFlowSet() {
		return userNetFlowMap.entrySet();
	}
	
	public static class UserNetFlow {
		public String password;
		public AtomicLong netIn = new AtomicLong(0);
		public AtomicLong netOut = new AtomicLong(0);
	}


}
