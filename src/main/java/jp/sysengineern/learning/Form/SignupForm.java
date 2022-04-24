package jp.sysengineern.learning.Form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SignupForm {
    @Pattern(regexp="^\\w{3,32}$", message="3文字以上、32文字以下で入力してください。 (A-Za-z0-9_)")
    private String username;

    @Size(min=8,max=32,message="パスワードは8文字以上、32文字以下で入力してください。")
    private String password;

    private String checkpassword;

    @Size(min=3,max=255)
    private String mailAddress;

    private boolean checkBox;
}
