package jp.sysengineern.learning.Entity;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Entity
@Table(name="users")
public class Users {
    //createdAtのために最初に呼ばれて現在時刻を取得する。
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    //ユーザーID、ユニーク
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long userId;

    //ユーザー名、ユニーク
    @Column(nullable=false,unique=true)
    private String username;

    //パスワードDB内ではハッシュ化
    @Column(nullable=false)
    private String password;

    //メールアドレス
    @Column(nullable=false,unique=true)
    private String mailAddress;

    //メールアドレス到達チェック未、済
    @Column(nullable=false)
    private boolean mailAddressVerified;

    //メールアドレス確認用uuid
    @Column(nullable=false)
    private String uuid;

    //ユーザーアカウント作成日
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    //有料会員、今は使わない
    @Column(nullable=true)
    private boolean isPaid;

    //デルフラグ
    @Column(nullable=false)
    private String delFlg;

    //ユーザー権限
    public enum Authority {ROLE_USER, ROLE_ADMIN}


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Set<Authority> authorities;

    public boolean isAdmin() {
        return this.authorities.contains(Authority.ROLE_ADMIN);
    }

    public void setAdmin(boolean isAdmin) {
        if (isAdmin) {
            this.authorities.add(Authority.ROLE_ADMIN);
        } else {
            this.authorities.remove(Authority.ROLE_ADMIN);
        }
    }

    //----リレーション 依存関係
    //コミュニティ内でのメッセージ
    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<Message> message;

    //コミュニティ内メッセージへの返信
    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<Reply> reply;

    //コース上でのメッセージ
    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<CourceMessage> courceMessage;

    //コース上でのメッセージへの返信
    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<CourceReply> courceReply;

    //相互フォローユーザーのダイレクトメッセージ
    @OneToMany(cascade = CascadeType.ALL,mappedBy="senderUser")
    @Column
    private List<DirectMessage> directMessage;

    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<Progress> progress;

    @OneToMany(cascade = CascadeType.ALL,mappedBy="users")
    @Column
    private List<Complete> complete;

    //ユーザー補足情報
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL,mappedBy="users")
    private UserDetail userDetail;

    //フォロー機能用
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followRelation",
                joinColumns= {@JoinColumn(name = "userId")},
                inverseJoinColumns = {@JoinColumn(name="following_id")})
    private List<Users> following;

    @ManyToMany(mappedBy="following")
    private List<Users> followers;

    //ユーザーが講義をどこまで取得したか
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TakeCource",
                joinColumns = {@JoinColumn(name = "userId")},
                inverseJoinColumns = {@JoinColumn(name="courceItemId")})
    private Set<CourceItem> courceItem;

    //どのコミュニティに参加しているか、現在は使用しない
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "JoinCommunity",
                joinColumns = {@JoinColumn(name="userIdAtComm")},
                inverseJoinColumns = {@JoinColumn(name="communityId")})
    private Set<Community> community;
    //https://blog.eyallupu.com/2010/06/hibernate-exception-simultaneously.html

    protected Users() {}


    //ユーザー登録用コンストラクター
    public Users(String username, String password, String mailAddress, String uuid) {
        this.username = username;
        this.password = password;
        this.mailAddress = mailAddress;
        this.mailAddressVerified = false;
        this.authorities = EnumSet.of(Authority.ROLE_USER);
        this.uuid = uuid;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public boolean getMailAddressVerified() {
        return mailAddressVerified;
    }
    public void setMailAddressVerified(boolean mailAddressVerified) {
        this.mailAddressVerified = mailAddressVerified;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<Complete> getComplete(){
        return complete;
    }

    public List<Progress> getProgress(){
        return progress;
    }

    public void setComplete(List<Complete> complete) {
        this.complete = complete;
    }
    public void setProgress (List<Progress> progress) {
        this.progress = progress;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsPaid() {
        return isPaid;
    }
    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getDelFlg() {
        return delFlg;
    }
    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public List<Message> getMessage(){
        return message;
    }
    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public List<Reply> getReply(){
        return reply;
    }
    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public List<CourceMessage> getCourceMessage(){
        return courceMessage;
    }
    public void setCourceMessage(List<CourceMessage> courceMessage) {
        this.courceMessage = courceMessage;
    }

    public List<CourceReply> getCourceReply(){
        return courceReply;
    }
    public void setCourceReply(List<CourceReply> courceReply) {
        this.courceReply = courceReply;
    }

    public List<DirectMessage> getDirectMessage(){
        return directMessage;
    }
    public void setDirectMessage(List<DirectMessage> directMessage) {
        this.directMessage = directMessage;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }
    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<Users> getFollowing(){
        return following;
    }
    public void setFollowing(List<Users> following) {
        this.following = following;
    }

    public List<Users> getFollowers(){
        return followers;
    }
    public void setFollowers(List<Users> followers) {
        this.followers = followers;
    }

    public Set<CourceItem> getCourceItem(){
        return courceItem;
    }
    public void setCourceItem(Set<CourceItem> courceItem) {
        this.courceItem = courceItem;
    }

    public Set<Community> getCommunity(){
    return community;
    }
    public void setCommnity(Set<Community> community) {
        this.community = community;
    }


}
