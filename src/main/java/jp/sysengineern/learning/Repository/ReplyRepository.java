package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findAll();

    List<Reply> findByMessage(long messageId);

    void deleteByReplyId(Long replyId);
}
