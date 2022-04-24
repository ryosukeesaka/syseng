package jp.sysengineern.learning.Form;

import lombok.Data;

@Data
public class FollowFollowerForm {

	private Long userId;

	private Long otherUserId;

	private Long communityId;

}
