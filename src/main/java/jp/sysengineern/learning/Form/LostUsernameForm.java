package jp.sysengineern.learning.Form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LostUsernameForm {
    @Size(min=1,max=32,message="パスワードを入力してください。")
    private String password;

    @Size(min=8,max=255,message="Eメールアドレスを入力してください。")
    private String mailAddress;
}
