package jp.sysengineern.learning.Form;

import lombok.Data;

@Data
public class CourceMessageDeleteForm {
    private Long userId;

    private Long messageId;

    private String courceUrl;

    private String courceDetailUrl;
}
