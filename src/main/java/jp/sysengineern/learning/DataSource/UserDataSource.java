package jp.sysengineern.learning.DataSource;

import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;

public interface UserDataSource {

    Users findByUsername(String username);

    Users findByUserId(Long userId);

    void save(UserDetail userDetail);
}
