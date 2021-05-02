package ph.indorse.phonebook.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ph.indorse.phonebook.model.ResponseDTO;
import ph.indorse.phonebook.model.UserDTO;
import ph.indorse.phonebook.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/create")
  public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO newUser) {
    userService.createNewUser(newUser);
    return ResponseEntity.ok(new ResponseDTO("User Created!"));
  }

  /**
   * login user
   * @param userDTO
   * @return
   */
  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {
    return ResponseEntity.ok(userService.login(userDTO.getUsername(), userDTO.getPassword()));

  }

  /**
   * search for users
   * @param firstName
   * @param lastName
   * @param userName
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<List<UserDTO>> search(
      @RequestParam(value = "firstname", required = false) String firstName,
      @RequestParam(value = "lastname", required = false) String lastName,
      @RequestParam(value = "username", required = false) String userName) {

    return ResponseEntity.ok(userService.search(firstName, lastName, userName));

  }

  /**
   * add a friend
   * @param userId
   * @param friendUserName
   * @return
   */
  @PostMapping("/add-friend")
  public ResponseEntity<ResponseDTO> addFriend(
      @RequestParam(value = "user_id") Long userId,
      @RequestParam(value = "friend_username") String friendUserName) {

    String message = userService.addFriend(userId, friendUserName);
    return ResponseEntity.ok(new ResponseDTO(message));
  }

  /**
   * remove a friend
   * @param userId
   * @param friendUserName
   * @return
   */
  @PostMapping("/remove-friend")
  public ResponseEntity<ResponseDTO> removeFriend(
      @RequestParam(value = "user_id") Long userId,
      @RequestParam(value = "friend_username") String friendUserName) {
    String message = userService.removeFriend(userId, friendUserName);
    return ResponseEntity.ok(new ResponseDTO(message));
  }






}
