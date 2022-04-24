package jp.sysengineern.learning.Form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EmailAddressCheckForm {

    private String mailAddress;

    @Size(min=1,max=255,message="メールアドレスを入力してください。")
    private String updateMailAddress;

}
