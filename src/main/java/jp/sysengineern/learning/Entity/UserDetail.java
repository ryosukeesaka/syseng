package jp.sysengineern.learning.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="userDetail")
public class UserDetail {


    //ID、ユニーク
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long userDescriptionId;

    //ユーザー説明
    @Column
    private String userDescription;

    //一言コメント
    @Column
    private String userShortDescription;

    //サムネイルパス
    @Column
    private String imgPath;

    //表示名、ユニークでない
    @Column
    private String userDisplayName;

    //ユーザーの保有ポイント、使わない
    @Column
    private String userPoint;

    //リレーションカラム
    @OneToOne
    private Users users;

    //コンストラクター、UserSecurityService用
    public UserDetail(Users users) {
        this.users = users;
    }
}
