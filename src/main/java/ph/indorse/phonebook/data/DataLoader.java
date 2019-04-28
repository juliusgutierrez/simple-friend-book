package ph.indorse.phonebook.data;

import javax.annotation.PostConstruct;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ph.indorse.phonebook.dao.FriendshipRepository;
import ph.indorse.phonebook.dao.UserRepository;
import ph.indorse.phonebook.entity.Friendship;
import ph.indorse.phonebook.entity.User;


/**
 * this class will create initial users
 */
@Component
public class DataLoader {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FriendshipRepository friendshipRepository;

  @PostConstruct
  private void init() {

    userRepository.save(
        new User
            .UserBuilder()
            .username("pedropenduko")
            .password(BCrypt.hashpw("1Passw@rd", BCrypt.gensalt(12)))
            .firstName("pedro")
            .lastName("penduko")
            .build());

    userRepository.save(
        new User
            .UserBuilder()
            .username("mariamakiling")
            .password(BCrypt.hashpw("2Passw@rd", BCrypt.gensalt(12)))
            .firstName("maria")
            .lastName("makiling")
            .build());

    userRepository.save(
        new User
            .UserBuilder()
            .username("juantamad")
            .password(BCrypt.hashpw("3Passw@rd", BCrypt.gensalt(12)))
            .firstName("juan")
            .lastName("tamad")
            .build());

    User pedro = new User();
    pedro.setId(1L);

    User maria = new User();
    maria.setId(2L);

    Friendship friendship = new Friendship();
    friendship.setUser(pedro);
    friendship.setFriend(maria);
    friendshipRepository.save(friendship);
  }

}
