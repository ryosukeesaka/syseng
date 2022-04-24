package jp.sysengineern.learning.Entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="courceItem")
public class CourceItem {

    //ID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long courceItemId;

    //コースアイテム名
    @Column
    private String courceItemName;

    //url
    @Column
    private String url;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    @Column
    private boolean isTest;

    @Column
    private boolean isMiniTest;

    //リレーションカラム
    @ManyToOne
    private CourceDetail courceDetail;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy="courceItem")
    @Column
    private List<CourceMessage> courceMessage;

    //リレーションカラム
    @ManyToMany(mappedBy="courceItem")
    private Set<Users> users;

}
