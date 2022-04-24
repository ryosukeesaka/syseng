package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Community;
import jp.sysengineern.learning.Entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
	  List<Message> findAll();

	  Message findByMessageId(long messageId);

	  Page<Message> findByCommunity(Community community,Pageable pageable);

	  void deleteByMessageId(Long messageId);

    Page<Message> findByCommunityOrderByCreatedAtDesc(Community community, Pageable pageable);
}
