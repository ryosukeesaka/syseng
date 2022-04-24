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
@Table(name="courceMessage")
public class CourceMessage {
    //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    //ID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long messageId;

    //本文
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
    @ManyToOne
    private CourceDetail courceDetail;

    //リレーションカラム
    @ManyToOne
    private CourceItem courceItem;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courceMessage")
    @Column
    private List<CourceReply> courceReply;

    public CourceMessage(Users users, String messageBody, CourceDetail courceDetail) {
        this.users = users;
        this.messageBody = messageBody;
        this.courceDetail = courceDetail;
        this.delFlg = "0";
    }
}
