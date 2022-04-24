package jp.sysengineern.learning.Form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LostPasswordForm {
    @Size(min=1,max=255,message="メールアドレスをご入力ください。")
    private String mailAddress;

    private String uuid;

    @Size(min=8,max=32,message="パスワードは8文字以上,32文字以下で入力してください。")
    private String password;

    private String checkPassword;

}
