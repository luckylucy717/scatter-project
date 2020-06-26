package com.lucy.scatter.service;

import java.util.HashMap;
import java.util.List;

public interface ScatterService {
	
	public List<HashMap<String, Object>> getScatterHistory(HashMap<String, Object> param) throws Exception;
	
	public int scatterMoney(HashMap<String, Object> param) throws Exception;
	
	public HashMap<String, Object> receiveMoney(HashMap<String, Object> param) throws Exception;
}
