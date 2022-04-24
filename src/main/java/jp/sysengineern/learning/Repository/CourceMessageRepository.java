package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.CourceDetail;
import jp.sysengineern.learning.Entity.CourceMessage;

@Repository
public interface CourceMessageRepository extends JpaRepository<CourceMessage,Long> {
    List<CourceMessage> findAll();

    List<CourceMessage> findByCourceDetail(CourceDetail courceDetail);

    CourceMessage findByMessageId(Long messageId);

    void deleteByMessageId(Long messageId);
}
