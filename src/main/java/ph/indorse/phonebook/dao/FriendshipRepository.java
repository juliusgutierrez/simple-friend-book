package ph.indorse.phonebook.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import ph.indorse.phonebook.entity.Friendship;

public interface FriendshipRepository extends CrudRepository<Friendship, Long>,
    QueryByExampleExecutor<Friendship> {

}
