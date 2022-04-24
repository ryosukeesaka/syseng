package jp.sysengineern.learning.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reply")
public class Reply {
    //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }
    //ID、ユニーク
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long replyId;

    //返信内容
    @Column
    private String replyBody;

    //作成日
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //リレーションカラム
    @ManyToOne
    private Users users;

    //リレーションカラム
    @ManyToOne
    private Message message;

    //コミュニティ内、メッセージへの返信用コンストラクタ
    public Reply(String replyBody,Message message, Users users){
    	this.replyBody = replyBody;
    	this.delFlg = "0";
    	this.message = message;
    	this.users = users;

    }
}
