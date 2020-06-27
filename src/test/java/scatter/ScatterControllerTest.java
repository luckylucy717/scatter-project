package scatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:spring/context-*.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScatterControllerTest {

	protected MockMvc mockMvc;
	
	@Autowired
	protected WebApplicationContext context;
	
	private String token;
	private String roomId 	= "room123";
	private Long scatterUserId 	= 77L;
	private Long receiverUserId = 28L;

	private String totalCost  = "20000";
	private String memberCnt  = "7";
	
	@Before
	public void setUp() throws Exception{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void test010_scatterMoney() throws Exception{
		
		MockHttpServletRequestBuilder scatterRequest = post("/money/scatter")
				.header("X-ROOM-ID", roomId)
				.header("X-USER-ID", scatterUserId)
				.param("total_cost", totalCost)
				.param("member_cnt", memberCnt);
				
		MvcResult result = this.mockMvc.perform(scatterRequest)
										.andDo(print())
										.andExpect(status().isOk())
										.andExpect(jsonPath("$.responseData.token").exists())
										.andReturn();

		String content = result.getResponse().getContentAsString();
	       
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> data = mapper.readValue(content, new TypeReference<HashMap<String, Object>>(){});
		
		token = (String) data.get("token");
	}
	
	@Test
	public void test020_receiveMoney_1() throws Exception{
		MockHttpServletRequestBuilder req = post("/money/receive")
				.header("X-ROOM-ID", roomId)
				.header("X-USER-ID", receiverUserId)
				.param("token", token);
		
				
		this.mockMvc.perform(req)
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.responseData.receive_money").exists())
					.andReturn();
	}
	
	@Test
	public void test021_receiveMoney_1Dup() throws Exception{
		this.test020_receiveMoney_1();
	}
	
	@Test
	public void test021_receiveMoney_2() throws Exception{
		MockHttpServletRequestBuilder req = post("/money/receive")
				.header("X-ROOM-ID", roomId)
				.header("X-USER-ID", scatterUserId)
				.param("token", token);
	
		this.mockMvc.perform(req)
						.andDo(print())
						.andExpect(status().isBadRequest())
						.andReturn();
	}
	
	@Test
	public void test030_showHistory() throws Exception{
		MockHttpServletRequestBuilder req = get("/history")
				.header("X-ROOM-ID", roomId)
				.header("X-USER-ID", scatterUserId)
				.param("token", token);
		
		this.mockMvc.perform(req)
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
	}
}
