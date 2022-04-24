package jp.sysengineern.learning.ServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Entity.Users.Authority;

public class UserDetailsImpl implements UserDetails {
//インターフェイスをオーバーロード
    private final Users users;

    public UserDetailsImpl (Users users) {
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : this.users.authorities) {
            authorities.add(new SimpleGrantedAuthority(authority.toString()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.users.getPassword();
    }

    @Override
    public String getUsername() {
        return this.users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getUserId() {
        return this.users.getUserId();
    }

    public String getMailAddress() {
        return this.users.getMailAddress();
    }

}
