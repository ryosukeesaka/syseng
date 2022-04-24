package jp.sysengineern.learning.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cource")
public class Cource {
    //ID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long courceId;

    //コース名
    @Column
    private String courceName;

    @Column
    private String courceDescription;

    //url
    @Column
    private String url;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy="cource")
    @Column
    private List<CourceTitle> courceTitle;
}
