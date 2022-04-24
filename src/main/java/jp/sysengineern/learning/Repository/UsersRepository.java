package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Community;
import jp.sysengineern.learning.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    List<Users> findAll();

    Users findByUsername(String username);

    Users findByUserId(Long userId);

    List<Users> findByUuidEquals(String uuid);

    Users findByUuid(String uuid);

    Users findByMailAddress(String mailAddress);

    List<Users> findByCommunity(Community community);


}
