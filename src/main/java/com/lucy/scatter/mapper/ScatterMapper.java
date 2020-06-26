package com.lucy.scatter.mapper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("scatterMapper")
public class ScatterMapper extends SqlSessionDaoSupport{
	
	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSession){
		super.setSqlSessionFactory(sqlSession);
	}
	
	public List<HashMap<String, Object>> selectScatterHistoryList(HashMap<String, Object> param){
		return this.getSqlSession().selectList("mapper.ScatterMapper.selectScatterHistoryList", param);
	}
	
	public int scatterMoney(HashMap<String, Object> param){
		return this.getSqlSession().insert("mapper.ScatterMapper.scatterMoney", param);
	}
	
	public HashMap<String, Object> selectUnReceivedMoney(HashMap<String, Object> param){
		return this.getSqlSession().selectOne("mapper.ScatterMapper.selectUnReceivedMoney", param);
	}
	public int receiveMoney(HashMap<String, Object> param){
		return this.getSqlSession().update("mapper.ScatterMapper.receiveMoney", param);
	}
}
