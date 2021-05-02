package transactions;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguel.restservice.RestServiceApplication;

//@RunWith(JUnitPlatform.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
@WebAppConfiguration
public  class TransactionControllerTest {
   protected MockMvc mvc;
   @Autowired
   WebApplicationContext TransactionController;
   
   @Before
   public void setUp() {
      mvc = MockMvcBuilders.webAppContextSetup(TransactionController).build();
   }
   
   @Test
	public void contextLoads() {
		assertNotNull(TransactionController);
	}
   
   //Next 5 Methods Verify the statuses and bodies for POST /transactions
   
   @Test
   public void transactionIsValid() throws Exception {
	   
      Transaction txn = new Transaction(new BigDecimal("12.334"), Date.from(Instant.now()));
      String inputJson = mapToJson(txn);
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transactions")
         .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(201, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   @Test
   public void whenTransactionOlderThanSixtySeconds() throws Exception {
	      
      Transaction txn = new Transaction(new BigDecimal("12.334"), Date.from(Instant.now().minus(61, ChronoUnit.SECONDS)));
      String inputJson = mapToJson(txn);
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transactions")
         .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(204, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   @Test
   public void ifJsonIsInvalid() throws Exception {
	  
      String inputJson = "{\"amount: 13.3343, \"timesp\": \"20-05-02}";
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transactions")
         .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(400, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   @Test
   public void ifFieldsAreNotParsable() throws Exception {
	  
      String inputJson = "{\"amount\": 13.3343, \"timestamp\": \"2021-05-02\"}";
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transactions")
         .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(422, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   @Test
   public void ifFutureTransactionDate() throws Exception {
	      
      Transaction txn = new Transaction(new BigDecimal("12.334"), Date.from(Instant.now().plus(5,ChronoUnit.SECONDS)));
      String inputJson = mapToJson(txn);
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transactions")
         .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(422, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   //Next Method verifies the status for DELETE /transactions
   
   @Test
   public void checkDeleteRequestStatus() throws Exception {
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/transactions")).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(204, status);
      String content = mvcResult.getResponse().getContentAsString();
      assertEquals(content, "");
   }
   
   protected String mapToJson(Object obj) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(obj);
   }
   
   protected <T> T mapFromJson(String json, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {
      
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, clazz);
   }
}