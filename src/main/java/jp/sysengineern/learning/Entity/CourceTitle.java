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
@Table(name="courceTitle")
public class CourceTitle {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long courceTitleId;

    //コースタイトル
    @Column
    private String courceTitleName;

    @Column
    private String courceTitleDescription;

    //url
    @Column
    private String url;

    //リレーションカラム
    @ManyToOne
    private Cource cource;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "courceTitle")
    @Column
    private List<CourceDetail> courceDetail;
}
