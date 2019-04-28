package ph.indorse.phonebook.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ph.indorse.phonebook.dao.FriendshipRepository;
import ph.indorse.phonebook.dao.UserRepository;
import ph.indorse.phonebook.entity.Friendship;
import ph.indorse.phonebook.entity.User;
import ph.indorse.phonebook.exception.PhoneBookException;
import ph.indorse.phonebook.model.UserDTO;
import ph.indorse.phonebook.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private static final String PASSWORD_PATTERN =
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FriendshipRepository friendshipRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public void createNewUser(UserDTO newUser) {
    validateUserInputs(newUser);
    User user = convertToEntity(newUser);
    userRepository.save(user);
  }

  @Override
  public UserDTO login(String username, String password) {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new PhoneBookException("Invalid login"));

    if (!BCrypt.checkpw(password, user.getPassword())) {
      throw new PhoneBookException("Invalid login");
    }

    return convertToDTO(user);

  }

  @Override
  public List<UserDTO> search(String firstName, String lastName, String userName) {
    User user = new User();

    if (firstName != null) {
      user.setFirstName(firstName);
    }

    if (lastName != null) {
      user.setLastName(lastName);
    }

    if (userName != null) {
      user.setUsername(userName);
    }

    Iterable<User> userList = userRepository.findAll(Example.of(user));
    return StreamSupport.stream(userList.spliterator(), false)
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public String addFriend(Long userId, String friendUserName) {

    userRepository.findById(userId)
        .orElseThrow(() -> new PhoneBookException("Failed, Invalid user_id"));

    User potentialFriend = userRepository.findByUsername(friendUserName)
        .orElseThrow(() -> new PhoneBookException(
            "Failed, User does not exist, invite them to create profile"));

    if (potentialFriend.getId().longValue() == userId) {
      throw new PhoneBookException(
          "Failed, You can't add yourself as friend, go outside and make friends!");
    }

    User currentUser = new User();
    currentUser.setId(userId);

    User newFriend = new User();
    newFriend.setId(potentialFriend.getId());

    Friendship friendship = new Friendship();
    friendship.setUser(currentUser);
    friendship.setFriend(newFriend);

    if (friendshipRepo.findOne(Example.of(friendship)).isPresent()) {
      throw new PhoneBookException(
          String.format("You already made friend with %s", friendUserName));
    }

    // add as friend
    friendshipRepo.save(friendship);
    return String.format("Success, you are now friends with %s", friendUserName);
  }

  @Override
  public String removeFriend(Long userId, String friendUserName) {

    userRepository.findById(userId)
        .orElseThrow(() -> new PhoneBookException("Failed, Invalid user_id"));

    User potentialFriend = userRepository.findByUsername(friendUserName)
        .orElseThrow(() -> new PhoneBookException(
            String.format("Failed, User does not exist, are you sure your friend with %s",
                friendUserName)));

    if (userId.longValue() == potentialFriend.getId()) {
      throw new PhoneBookException("Failed, You can't un-friend yourself");
    }

    User currentUser = new User();
    currentUser.setId(userId);

    User unfriend = new User();
    unfriend.setId(potentialFriend.getId());

    Friendship friendship = new Friendship();
    friendship.setUser(currentUser);
    friendship.setFriend(unfriend);

    Friendship friendShipOver = friendshipRepo
        .findOne(Example.of(friendship))
        .orElseThrow(() -> new PhoneBookException(
            "Failed, you can't un-friend that is not in your friend-list"));

    friendshipRepo.delete(friendShipOver);
    return String.format("Successfully un-friend %s", friendUserName);

  }


  private UserDTO convertToDTO(User user) {
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);

    //show all friends, no restrictions yet. don't show your friends password
    List<UserDTO> friendList = user.getFriendshipList().stream()
        .map(Friendship::getFriend)
        .map(friend -> {
          friend.setPassword(null);
          return modelMapper.map(friend, UserDTO.class);
        }).collect(Collectors.toList());

    userDTO.setFriendLists(friendList);
    //don't return the password, security purposes;
    userDTO.setPassword(null);
    return userDTO;
  }

  private User convertToEntity(UserDTO userDTO) {
    User user = modelMapper.map(userDTO, User.class);

    //update the password to hash
    String password = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));
    user.setPassword(password);

    return user;
  }

  /**
   * Validate only password and user is exist other validations should be done in front end;
   */
  private void validateUserInputs(UserDTO userDTO) {

    //must have password
    if (userDTO.getPassword() == null) {
      throw new PhoneBookException("password should not be null");
    }

    //must be password pattern
    if (!userDTO.getPassword().matches(getPasswordPattern())) {
      throw new PhoneBookException("password should have lowercase, "
          + "uppercase, number, special char, atleast 8 char");
    }

    //find user is already exist;
    userRepository.findByUsername(userDTO.getUsername())
        .ifPresent(user -> {
          throw new PhoneBookException("Username is already taken");
        });
  }

  /**
   * 1 digit, lowercase, uppercase, special char, atleast 8
   */
  private String getPasswordPattern() {
    return PASSWORD_PATTERN;
  }
}
