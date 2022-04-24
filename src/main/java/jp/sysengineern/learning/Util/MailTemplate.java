package jp.sysengineern.learning.Util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailTemplate {

    public SimpleMailMessage template1(String username,String mailAddress, String url) {
        //改行コード生成
        String br = System.getProperty("line.separator");
        //メールセンダーを取得
        SimpleMailMessage mail = new SimpleMailMessage();

        //メール本文
        String mailText =
                "(テスト）この度は、しすてむえんじに庵にご登録頂きまして"
                + br + "誠にありがとうございます。"
                + br
                + br + "お申し込み頂きましたアカウント情報は以下となります。"
                + br + "ユーザー名： " + username
                + br + "パスワード：個人情報のため表示を伏せています"
                + br + "ご本人様確認のため、下記URLへ「24時間以内」にアクセスし"
                + br + "アカウントの本登録を完了させて下さい。"
                + br + url
                + br
                + br + "※当メール送信後、24時間を超過しますと、セキュリティ保持のため有効期限切れとなります。"
                + br + "　その場合は再度、最初からお手続きをお願い致します。"
                + br
                + br + "※お使いのメールソフトによってはURLが途中で改行されることがあります。"
                + br + "　その場合は、最初の「http://」から末尾の英数字までをブラウザに"
                + br + "　直接コピー＆ペーストしてアクセスしてください。"
                + br
                + br + "※当メールは送信専用メールアドレスから配信されています。"
                + br + "　このままご返信いただいてもお答えできませんのでご了承ください。"
                + br + ""
                + br + "※当メールに心当たりの無い場合は、誠に恐れ入りますが"
                + br + "　破棄して頂けますよう、よろしくお願い致します。"
                + br
                + br + "（会社情報必要であれば）";

        //メールの中身
        mail.setFrom("regist@sys-engineern.jp");//送信者
        mail.setTo(mailAddress);//受信者（ユーザー）
        mail.setBcc("regist@sys-engineern.jp");
        mail.setSubject("しすてむえんじに庵 ユーザー登録テストメール");//メールタイトル
        mail.setText(mailText);//メール本文
        return mail;
    }

    public SimpleMailMessage passwordReset(String username,String mailAddress, String url) {
        //改行コード生成
        String br = System.getProperty("line.separator");
        //メールセンダーを取得
        SimpleMailMessage mail = new SimpleMailMessage();

        //メール本文
        String mailText =
                "(テスト）しすてむえんじに庵のパスワードリセットの"
                + br + "申請を受け付けました。"
                + br
                + br + "パスワード再設定をご希望の場合は、以下のURLへ「4時間以内」にアクセスし"
                + br + "新しいパスワードをご登録ください。"
                + br
                + br + url
                + br
                + br + "※当メール送信後、4時間を超過しますと、セキュリティ保持のため有効期限切れとなります。"
                + br + "　その場合は再度、最初からお手続きをお願い致します。"
                + br
                + br + "※お使いのメールソフトによってはURLが途中で改行されることがあります。"
                + br + "　その場合は、最初の「http://」から末尾の英数字までをブラウザに"
                + br + "　直接コピー＆ペーストしてアクセスしてください。"
                + br
                + br + "※当メールは送信専用メールアドレスから配信されています。"
                + br + "　このままご返信いただいてもお答えできませんのでご了承ください。"
                + br + ""
                + br + "※当メールに心当たりの無い場合は、誠に恐れ入りますが"
                + br + "　破棄して頂けますよう、よろしくお願い致します。"
                + br
                + br + "（会社情報必要であれば）";

        //メールの中身
        mail.setFrom("regist@sys-engineern.jp");//送信者
        mail.setTo(mailAddress);//受信者（ユーザー）
        mail.setBcc("regist@sys-engineern.jp");
        mail.setSubject("しすてむえんじに庵 パスワードリセットのお知らせ（テスト）");//メールタイトル
        mail.setText(mailText);//メール本文
        return mail;
    }

    public SimpleMailMessage inquiryUsername(String username,String mailAddress) {
        //改行コード生成
        String br = System.getProperty("line.separator");
        //メールセンダーを取得
        SimpleMailMessage mail = new SimpleMailMessage();

        //メール本文
        String mailText =
                "(テスト）しすてむえんじに庵のユーザー名照会リクエストを"
                + br + "受け付けました。"
                + br
                + br + "あなたのユーザー名は"
                + br
                + br + username
                + br
                + br + "※当メールは送信専用メールアドレスから配信されています。"
                + br + "　このままご返信いただいてもお答えできませんのでご了承ください。"
                + br + ""
                + br + "※当メールに心当たりの無い場合は、誠に恐れ入りますが"
                + br + "　破棄して頂けますよう、よろしくお願い致します。"
                + br
                + br + "（会社情報必要であれば）";

        //メールの中身
        mail.setFrom("regist@sys-engineern.jp");//送信者
        mail.setTo(mailAddress);//受信者（ユーザー）
        mail.setBcc("regist@sys-engineern.jp");
        mail.setSubject("しすてむえんじに庵 ユーザー名照会リクエスト（テスト）");//メールタイトル
        mail.setText(mailText);//メール本文
        return mail;
    }
}
