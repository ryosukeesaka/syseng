package jp.sysengineern.learning.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sysengineern.learning.DataSource.UserDataSource;
import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDataSource userDataSource;

	@Override
	public Users findByUsername(String username) {
		Users user = userDataSource.findByUsername(username);
		return user;
	}

	@Override
	public Users findByUserId(Long userId) {
		Users user = userDataSource.findByUserId(userId);
		return user;
	}

	@Override
	public void save(UserDetail userDetail) {
	    userDataSource.save(userDetail);
	}
}
