package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.CourceReply;
@Repository
public interface CourceReplyRepository extends JpaRepository<CourceReply,Long> {
    List<CourceReply> findAll();

    void deleteByReplyId(Long replyId);
}
