package jp.sysengineern.learning.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.terasoluna.gfw.web.mvc.support.CompositeRequestDataValueProcessor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenRequestDataValueProcessor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Bean
    public TransactionTokenInterceptor transactionTokenIntercepter() {
        return new TransactionTokenInterceptor();
    }

    //二重送信防止機能
    @Bean
    public RequestDataValueProcessor requestDataValueProcessor() {
        return new CompositeRequestDataValueProcessor(new CsrfRequestDataValueProcessor(), new TransactionTokenRequestDataValueProcessor());
    }

    //共通の処理を行うクラスを実装したい,といった際に使うクラスです。
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionTokenIntercepter()).addPathPatterns("/**");
    }

}
