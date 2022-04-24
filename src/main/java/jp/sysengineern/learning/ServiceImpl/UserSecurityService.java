package jp.sysengineern.learning.ServiceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Repository.UserDetailRepository;
import jp.sysengineern.learning.Repository.UsersRepository;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailRepository userDetailRepository;

    //loadUserBtUsername 名前を用いてユーザー検索
    @Override//userdetails ユーザー名とパスワードを保存しアカウント情報を出力
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("ユーザー名が空です。");
        }

        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザー名が見つかりません: " + username);
        }

        return new UserDetailsImpl(user);//newしたクラスのインスタンスを返している。controllerは戻り値で画面に。その他は他のクラスに渡す
    }//userdetailsを継承しているuserdetailsimplを返す

    @Transactional
    public String registerAdmin(String username, String password, String mailAddress) {
        UUID uuid_ = UUID.randomUUID();//IDをランダムに生成
        String uuid = uuid_.toString();

        Users user = new Users(username, passwordEncoder.encode(password), mailAddress, uuid);
        user.setAdmin(true);
        user.setDelFlg("0");
        usersRepository.save(user);

        String url = "http://localhost:8080/mailAddress_Verified_Check/" + uuid;

        return url;
    }

    @Transactional//LoginController
    public String registerUser(String username, String password, String mailAddress) {
        UUID uuid_ = UUID.randomUUID();
        String uuid = uuid_.toString();

        Users user = new Users(username, passwordEncoder.encode(password), mailAddress, uuid);
        user.setDelFlg("0");
        usersRepository.save(user);
        Users newUser = usersRepository.findByUsername(username);
        UserDetail userDesc = new UserDetail(newUser);
        userDetailRepository.save(userDesc);


        String url = "http://localhost:8080/mailAddress_Verified_Check/" + uuid;

        return url;
    }


    @Transactional(readOnly = false)
    public String mailAddressVerifiedCheck(String uuid) {
        List<Users> isExist =usersRepository.findByUuidEquals(uuid);
        String msg = "";

        if(!isExist.isEmpty()) {
            Users user = usersRepository.findByUuid(uuid);
            user.setMailAddressVerified(true);
            usersRepository.save(user);
            msg = "ユーザー登録完了が完了しました。";
        }else {
            msg = "URLに間違いがあるようです。ご確認ください。";
        }
        return msg;
    }
}
