package jp.sysengineern.learning.Form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MyPageUserEditForm {

	private String userDisplayName;

	private String userShortDescription;

	private String userDescription;

    private MultipartFile uploadFile;

}
