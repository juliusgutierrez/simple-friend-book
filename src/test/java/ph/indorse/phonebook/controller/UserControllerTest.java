package ph.indorse.phonebook.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ph.indorse.phonebook.model.UserDTO;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class UserControllerTest extends ControllerTest {

  @Test
  public void create_user_success() throws Exception {

    UserDTO test = new UserDTO();
    test.setUsername("test");
    test.setPassword("1Passw@rd");
    test.setFirstName("IamFirstName");
    test.setLastName("IamLastName");

    mvc().perform(MockMvcRequestBuilders.post("/user/create")
        .content(mapToJson(test))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", containsString("User Created!")));
  }

  @Test
  public void create_user_already_exist_username() throws Exception {
    UserDTO test = new UserDTO();
    test.setUsername("pedropenduko");
    test.setPassword("1Passw@rd");
    test.setFirstName("IamFirstName");
    test.setLastName("IamLastName");

    mvc().perform(MockMvcRequestBuilders.post("/user/create")
        .content(mapToJson(test))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", containsString("Username is already taken")));
  }

  @Test
  public void create_user_password_empty() throws Exception {
    UserDTO test = new UserDTO();
    test.setUsername("test");
    test.setPassword(null);
    test.setFirstName("IamFirstName");
    test.setLastName("IamLastName");

    mvc().perform(MockMvcRequestBuilders.post("/user/create")
        .content(mapToJson(test))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", containsString("password should not be null")));
  }

  @Test
  public void create_user_wrong_password_format() throws Exception {
    UserDTO test = new UserDTO();
    test.setUsername("test");
    test.setPassword("password");
    test.setFirstName("IamFirstName");
    test.setLastName("IamLastName");

    mvc().perform(MockMvcRequestBuilders.post("/user/create")
        .content(mapToJson(test))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message",
            containsString(
                "password should have lowercase, uppercase, number, special char, atleast 8 char")));
  }


  @Test
  public void login_success() throws Exception {
    UserDTO pedro = new UserDTO();
    pedro.setUsername("pedropenduko");
    pedro.setPassword("1Passw@rd");

    mvc().perform(MockMvcRequestBuilders.post("/user/login")
        .content(mapToJson(pedro))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().json(
            jsonResource("ph/indorse/phonebook/usercontroller/login_success.json")));
  }

  @Test
  public void invalid_username() throws Exception {
    UserDTO pedro = new UserDTO();
    pedro.setUsername("invalid");
    pedro.setPassword("1Passw@rd");

    mvc().perform(MockMvcRequestBuilders.post("/user/login")
        .content(mapToJson(pedro))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", containsString("Invalid login")));
  }

  @Test
  public void invalid_password() throws Exception {
    UserDTO pedro = new UserDTO();
    pedro.setUsername("pedropenduko");
    pedro.setPassword("1Passw@rd2");

    mvc().perform(MockMvcRequestBuilders.post("/user/login")
        .content(mapToJson(pedro))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", containsString("Invalid login")));
  }

  @Test
  public void search_success() throws Exception {
    mvc().perform(MockMvcRequestBuilders.get("/user/search")
        .param("firstname", "maria")
        .param("lastname", "makiling")
        .param("username", "mariamakiling"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userId").exists());
  }

  @Test
  public void add_friend_success() throws Exception {
    mvc().perform(MockMvcRequestBuilders.post("/user/add-friend")
        .param("user_id", "2")
        .param("friend_username", "pedropenduko"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message",
            containsString("Success, you are now friends with pedropenduko")));
  }

  @Test
  public void add_friend_failed_cant_add_yourself() throws Exception {
    mvc().perform(MockMvcRequestBuilders.post("/user/add-friend")
        .param("user_id", "1")
        .param("friend_username", "pedropenduko"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message",
            containsString(
                "Failed, You can't add yourself as friend, go outside and make friends!")));
  }

  @Test
  public void add_friend_failed_already_your_friend() throws Exception {
    mvc().perform(MockMvcRequestBuilders.post("/user/add-friend")
        .param("user_id", "1")
        .param("friend_username", "mariamakiling"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message",
            containsString(
                "You already made friend with mariamakiling")));
  }

  @Test
  public void remove_friend_success() throws Exception {
    mvc().perform(MockMvcRequestBuilders.post("/user/remove-friend")
        .param("user_id", "1")
        .param("friend_username", "mariamakiling"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message",
            containsString("Successfully un-friend mariamakiling")));
  }

  @Test
  public void unfriend_not_friendlist() throws Exception {
    final String friendUserName = "notafriend";
    mvc().perform(MockMvcRequestBuilders.post("/user/remove-friend")
        .param("user_id", "1")
        .param("friend_username", friendUserName))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message",
            containsString(
                "Failed, User does not exist, are you sure your friend with " + friendUserName)));
  }

  @Test
  public void unfriend_not_urself() throws Exception {
    mvc().perform(MockMvcRequestBuilders.post("/user/remove-friend")
        .param("user_id", "1")
        .param("friend_username", "pedropenduko"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message",
            containsString("Failed, You can't un-friend yourself")));
  }


}
