package jp.sysengineern.learning.Form;

import java.util.Date;

import jp.sysengineern.learning.Entity.Message;
import jp.sysengineern.learning.Entity.Users;
import lombok.Data;

@Data
public class ReplyForm {

	private Long replyId;

	private String replyBody;

	private Date createdAt;

	private String delFlg;

	private Long messageId;

	private Message message;

	private Users users;

}
