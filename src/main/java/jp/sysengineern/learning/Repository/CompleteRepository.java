package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Complete;

@Repository
public interface CompleteRepository extends JpaRepository<Complete,Long> {
    List<Complete> findAll();

}
