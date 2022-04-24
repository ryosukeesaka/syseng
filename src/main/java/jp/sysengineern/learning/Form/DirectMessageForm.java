package jp.sysengineern.learning.Form;

import jp.sysengineern.learning.Entity.Users;
import lombok.Data;

@Data
public class DirectMessageForm {
	private String directMessageBody;

	private Users recieverUser;

	private Long recieverUserId;

}
