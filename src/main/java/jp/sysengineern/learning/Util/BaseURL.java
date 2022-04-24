package jp.sysengineern.learning.Util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="syseng")//プロパティクラスを定義するアノテーションではなく、 DIコンテナに登録されたBeanに対してプロパティを設定するためのアノテーション
//application.propertiesのsyseng
public class BaseURL {
    private String url;
  //application.propertiesのsyseng.imgUploadPath
    private String imgUploadPath;

    private String imgUploadPathBackUp;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImgUploadPath() {
        return imgUploadPath;
    }
    public void setImgUploadPath(String imgUploadPath) {
        this.imgUploadPath = imgUploadPath;
    }
    public String getImgUploadPathBackUp() {
        return imgUploadPathBackUp;
    }
    public void setImgUploadPathBackUp(String imgUploadPathBackUp) {
        this.imgUploadPathBackUp = imgUploadPathBackUp;
    }
}
