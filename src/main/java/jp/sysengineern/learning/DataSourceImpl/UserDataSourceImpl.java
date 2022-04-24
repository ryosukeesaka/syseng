package jp.sysengineern.learning.DataSourceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.sysengineern.learning.DataSource.UserDataSource;
import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Repository.UserDetailRepository;
import jp.sysengineern.learning.Repository.UsersRepository;
@Component
public class UserDataSourceImpl implements UserDataSource {

	@Autowired
	private UsersRepository usersRepository;
	private UserDetailRepository userDetailRepository;

	@Override
	public Users findByUsername(String username) {
		Users user = usersRepository.findByUsername(username);
		return user;
	}

	@Override
	public Users findByUserId(Long userId) {
		Users user = usersRepository.findByUserId(userId);
		return user;
	}


	@Override
	public void save(UserDetail userDetail) {
		userDetailRepository.save(userDetail);
	}

}
