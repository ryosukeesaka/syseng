package jp.sysengineern.learning.Entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="message")
public class Message {

  //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

  //ユーザーID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long messageId;

    //メッセージ本文
    @Column
    private String messageBody;

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
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "message")
    private List<Reply> reply;

    //リレーションカラム
    @ManyToOne
    private Community community;

    //コミュニティ内でのメッセージ送信用コンストラクタ
    public Message(String messageBody, Users users, Community community){
    	this.messageBody = messageBody;
    	this.delFlg = "0";
    	this.users = users;
    	this.community = community;
    }

}
