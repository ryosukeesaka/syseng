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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Table(name="community")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"users"})
public class Community {
    //ID、ユニーク
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long communityId;

    //コミュニティ名
    @Column
    private String communityName;

    //コミュニティ説明
    @Column
    private String communityDescription;

    //プライベートかどうか
    @Column
    private boolean isPrivate;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //リレーションカラム
    @ManyToMany(mappedBy="community")
    private Set<Users> users;

    //リレーションカラム
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "community")
    @Column
    private List<Message> message;

    public Long getCommunityId(){
        return communityId;
    }
    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityDescription() {
        return communityDescription;
    }
    public void setCommunityDescription(String communityDescription) {
        this.communityDescription = communityDescription;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getDelFlg() {
        return delFlg;
    }
    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }
    public Set<Users> getUsers(){
        return users;
    }
    public void setUsers(Set<Users> users) {
        this.users = users;
    }
    public List<Message> getMessage(){
        return message;
    }
    public void setMessage(List<Message> message) {
        this.message = message;
    }
}
