package com.lucy.scatter.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lucy.scatter.service.ScatterService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ScatterController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired 
	private ScatterService scatterService;
	 
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody List<HashMap<String, Object>> getScatterHistory(
			@RequestParam(value = "token", required = true) String token 
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", request.getHeader("X-ROOM-ID"));
		param.put("user_id", request.getHeader("X-USER-ID"));
		param.put("token", token);
		return scatterService.getScatterHistory(param); 
	}
	
	@RequestMapping(value = "/money/scatter", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody int scatterMoney(
			@RequestParam(value = "total_cost", required = true) Long totalCost, 
			@RequestParam(value = "member_cnt", required = true) Integer memberCnt
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", 	request.getHeader("X-ROOM-ID"));
		param.put("user_id", 	request.getHeader("X-USER-ID"));
		param.put("total_cost", totalCost);
		
		long [] moneyNoArr = new long[memberCnt];
		long res = totalCost % memberCnt;
		long share = totalCost / memberCnt;
		for(int i=0; i< memberCnt ; i++){
			moneyNoArr[i] =share;
			if(i == 0){
				moneyNoArr[i] = share + res;
			}
		}
		param.put("moneyNoArr", moneyNoArr);

		//토큰 발급
		param.put("token", generate());
		return scatterService.scatterMoney(param); 
	}
	
	private static SecureRandom random = new SecureRandom();
	
	public static final int tokenLen = 3;
	public static final String randomStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String generate() {
        StringBuilder sb = new StringBuilder(tokenLen);
        for (int i = 0; i < tokenLen; i++) {
            sb.append( randomStr.charAt(random.nextInt(randomStr.length())));
        }
        return sb.toString();
    }
	
	@RequestMapping(value = "/money/receive", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody HashMap<String, Object> receiveMoney(
			@RequestParam(value = "token", required = true) String token 
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", 	request.getHeader("X-ROOM-ID"));
		param.put("user_id", 	request.getHeader("X-USER-ID"));
		param.put("token", 		token);
		return scatterService.receiveMoney(param); 
	}
}
