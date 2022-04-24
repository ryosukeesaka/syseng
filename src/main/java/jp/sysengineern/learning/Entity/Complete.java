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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="complete")
@AllArgsConstructor
@NoArgsConstructor
public class Complete {
  //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long CompleteId;

    @Column
    private String CompleteItemId;

    @Column
    private String CompleteItemCount;

    @Column
    private Date createdAt;

    @Column
    private boolean isComplete;

    @ManyToOne
    private Users users;
}
