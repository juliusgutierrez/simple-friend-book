package ph.indorse.phonebook.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

  private Long userId;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private List<UserDTO> friendLists;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<UserDTO> getFriendLists() {
    return friendLists;
  }

  public void setFriendLists(List<UserDTO> friendLists) {
    this.friendLists = friendLists;
  }
}
