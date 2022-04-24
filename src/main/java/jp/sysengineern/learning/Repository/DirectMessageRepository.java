package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.DirectMessage;
import jp.sysengineern.learning.Entity.Users;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage,Long> {
    List<DirectMessage> findAll();

    List<DirectMessage> findBySenderUserInAndRecieverUserInOrderByCreatedAtAsc(List<Users> user,List<Users> recieverUser);

    Page<DirectMessage> findBySenderUserInAndRecieverUserInOrderByCreatedAtAsc(List<Users> user,List<Users> recieverUser,Pageable pageable2);

}
