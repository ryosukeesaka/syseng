package jp.sysengineern.learning.Form;

import lombok.Data;

@Data
public class CourceMessageForm {
    private String messageBody;

    private Long courceDetailId;

    private String courceUrl;

}
