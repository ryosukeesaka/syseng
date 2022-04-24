package jp.sysengineern.learning.Service;

import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;

public interface UserService {

	Users findByUsername(String username);

	Users findByUserId(Long userId);

	void save(UserDetail userDetail);
}
