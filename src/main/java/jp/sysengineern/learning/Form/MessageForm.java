package jp.sysengineern.learning.Form;

import java.util.Date;

import lombok.Data;

@Data
public class MessageForm {

	private Long messageId;

	private String messageBody;

	private Date createdAt;

	private String delFlg;

	private Long community_community_id;

	private Long users_user_id;

}
