package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress,Long> {

    List<Progress> findAll();

}
