package jp.sysengineern.learning.Form;

import lombok.Data;

@Data
public class CourceReplyDeleteForm {
    private String courceUrl;
    private String courceDetailUrl;
    private Long replyId;

    private Long courceMessageId;
}
