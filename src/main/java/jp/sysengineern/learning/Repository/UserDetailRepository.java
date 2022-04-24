package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.UserDetail;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,Long>{
    List<UserDetail> findAll();
}
