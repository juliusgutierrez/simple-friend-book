package ph.indorse.phonebook.dao;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import ph.indorse.phonebook.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, QueryByExampleExecutor<User> {

  Optional<User> findByUsername(String username);

}
