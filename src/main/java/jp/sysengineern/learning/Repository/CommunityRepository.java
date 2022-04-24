package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Community;
@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {
    List<Community> findAll();

    Community findByCommunityId(Long id);
}
