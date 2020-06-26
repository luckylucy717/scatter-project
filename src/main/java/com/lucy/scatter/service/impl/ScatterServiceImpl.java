package com.lucy.scatter.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucy.scatter.mapper.ScatterMapper;
import com.lucy.scatter.service.ScatterService;

@Service("scatterService")
public class ScatterServiceImpl implements ScatterService{

	@Autowired
	private ScatterMapper scatterMapper;
	
	@Override
	public List<HashMap<String, Object>> getScatterHistory(HashMap<String, Object> param) throws Exception {
		return scatterMapper.selectScatterHistoryList(param);
	}

	@Override
	public int scatterMoney(HashMap<String, Object> param) throws Exception {
		return scatterMapper.scatterMoney(param);
	}

	@Override
	public HashMap<String, Object> receiveMoney(HashMap<String, Object> param) throws Exception {
		
		Long receive_amount = 0L;
		
		HashMap<String, Object> unReceived = scatterMapper.selectUnReceivedMoney(param);
		if(unReceived !=null && !unReceived.isEmpty()){
			
			param.put("scatter_history_no", unReceived.get("scatter_history_no"));
			scatterMapper.receiveMoney(param);
			receive_amount = (Long) unReceived.get("scatter_cost");
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("receive_amount", receive_amount);
	
		return result;
	}
	
}
