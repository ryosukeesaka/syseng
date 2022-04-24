package jp.sysengineern.learning.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="courceDetail")
public class CourceDetail {
    //ID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long courceDetailId;

    //コース詳細名
    @Column
    private String courceDetailName;

    //有料コース=true、無料コース=false
    @Column
    private boolean isPaid;

    //url
    @Column
    private String url;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //リレーションカラム
    @ManyToOne
    private CourceTitle courceTitle;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "courceDetail")
    @Column
    private List<CourceItem> courceItem;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy="courceDetail")
    @Column
    private List<CourceMessage> courceMessage;
}
