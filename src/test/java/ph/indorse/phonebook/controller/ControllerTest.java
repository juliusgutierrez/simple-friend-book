package ph.indorse.phonebook.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class ControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Autowired
  ResourceLoader resourceLoader;

  @Before
  public void setUpControllerTest() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  public MockMvc mvc() {
    return this.mockMvc;
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  protected String jsonResource(String fileName) throws Exception {
    Resource resource = resourceLoader.getResource(String.format("classpath:%s", fileName));
    return IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
  }

}
