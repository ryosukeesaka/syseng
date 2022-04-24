package jp.sysengineern.learning.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.sysengineern.learning.ServiceImpl.UserSecurityService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserSecurityService userSecurityService;

    //-- WebSecurity class - ignoring全部許可
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**");
    }

    //-- HttpSecurity - URLごとに異なるセキュリティ設定を行う
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
        .authorizeRequests()

            .antMatchers( "/index","/signup").permitAll()
            //ユーザー権限でのアクセス可能ページ設定
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/admin/**").hasRole("ADMIN")
            //.anyRequest().authenticated()
            .and()
        .formLogin()//login処理
            .loginPage("/login").failureUrl("/login-error")//controllerのmappingに合わせる　失敗した際のリンク先
            .defaultSuccessUrl("/user/mypage/profile", false)//true トップへ false 指定URLへ
            .usernameParameter("username").passwordParameter("password")//htmlname属性
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")//ログアウト成功
            .deleteCookies("JSESSIONID")//cookie JSESSIONID削除
            .invalidateHttpSession(true)//セッション無効
            .permitAll();
    }

    //-- HttpSecurity - ここまで ---


    //-- configureGlobal設定 -- 先に呼ばれる login時のユーザー情報取得
    @Autowired//DBが未完成の時に使用する仮のもの
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
        .inMemoryAuthentication()//ユーザーをインメモリに保存 データをすべてメモリ上で処理することにより高速な処理ができる。これをデータベースで実現するのがインメモリデータベースである
            .withUser("user").password("password").roles("USER");
    }


    //-- AuthenticationManagerBuilder - 認証方法の実装の設定を行う
    @Override//DBからユーザーを検索
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
            .userDetailsService(userSecurityService)
            .passwordEncoder(passwordEncoder());
        //http://inaz2.hatenablog.com/entry/2017/04/18/222721
        //userSecurityService.registerAdmin("admin", "password", "admin@localhost");//adminが初期設定される。
        //userSecurityService.registerUser("user3", "password", "user3@localhost");
    }

    //-- AuthenticationManagerBuilder - ここまで ---


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
