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
@Table(name="directMessage")
public class DirectMessage {
    //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    //ID、ユニーク
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long directMessageId;

    //ダイレクトメッセージ本文
    @Column
    private String directMessageBody;

    //作成日
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    //既読フラグ
    @Column
    private boolean unread;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //送信者、リレーションカラム
    @ManyToOne
    private Users senderUser;

    //受信者、リレーションカラム
    @ManyToOne
    private Users recieverUser;

    //ダイレクトメッセージ送信用コンストラクター
    public DirectMessage(String directMessageBody, Users senderUser, Users recieverUser){
    	this.directMessageBody = directMessageBody;
    	this.unread = false;
    	this.delFlg = "0";
    	this.senderUser = senderUser;
    	this.recieverUser = recieverUser;
    }


}
