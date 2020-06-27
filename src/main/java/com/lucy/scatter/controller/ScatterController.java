package com.lucy.scatter.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lucy.scatter.model.Response;
import com.lucy.scatter.service.ScatterService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ScatterController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired 
	private ScatterService scatterService;
	
	/* 조회 */
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody ResponseEntity<Response> getScatterHistory(
			@RequestParam(value = "token", required = true) String token 
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", request.getHeader("X-ROOM-ID"));
		param.put("user_id", request.getHeader("X-USER-ID"));
		param.put("token", token);
		
		List<HashMap<String,Object>> list = scatterService.getScatterHistory(param);
		
		Response res = new Response();
		res.setResponseData(list);
		
		if(list !=null && list.size()>0){
			return new ResponseEntity<Response>(res, HttpStatus.OK); // 성공
		}else{
			return new ResponseEntity<Response>(res, HttpStatus.BAD_REQUEST); // 조회조건에 맞는 경우가 없을 때 
		}
	}
	
	/* 뿌리기 */
	@RequestMapping(value = "/money/scatter", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody ResponseEntity<Response> scatterMoney(
			@RequestParam(value = "total_cost", required = true) Long totalCost, 
			@RequestParam(value = "member_cnt", required = true) Integer memberCnt
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", 	request.getHeader("X-ROOM-ID"));
		param.put("user_id", 	request.getHeader("X-USER-ID"));
		param.put("total_cost", totalCost);
		
		long [] moneyNoArr = new long[memberCnt];
		long rest = totalCost % memberCnt;
		long share = totalCost / memberCnt;
		for(int i=0; i< memberCnt ; i++){
			moneyNoArr[i] =share;
			if(i == 0){
				moneyNoArr[i] = share + rest;
			}
		}
		param.put("moneyNoArr", moneyNoArr);

		//토큰 발급
		String token = generate();
		param.put("token", token);
		int resultCnt = scatterService.scatterMoney(param); 
		
		HashMap<String, Object> responseData = new HashMap<>(); 
		responseData.put("token", token);
		responseData.put("resultCnt", resultCnt);
		
		Response res = new Response();
		res.setResponseData(responseData);
		
		return new ResponseEntity<Response>(res, HttpStatus.OK);
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
	
	/* 받기 */
	@RequestMapping(value = "/money/receive", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody ResponseEntity<Response> receiveMoney(
			@RequestParam(value = "token", required = true) String token 
	) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("room_id", 	request.getHeader("X-ROOM-ID"));
		param.put("user_id", 	request.getHeader("X-USER-ID"));
		param.put("token", 		token);
		
		HashMap<String, Object> result = scatterService.receiveMoney(param);
		Long receive_money = (Long)result.get("receive_money");
		
		Response res = new Response();
		res.setResponseData(result);
		
		if(receive_money !=null && receive_money>0){
			return new ResponseEntity<Response>(res, HttpStatus.OK);
		}else{
			return new ResponseEntity<Response>(res, HttpStatus.BAD_REQUEST);
		}
	}
}
