 package jp.sysengineern.learning.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.EmailAddressCheckForm;
import jp.sysengineern.learning.Form.LostPasswordForm;
import jp.sysengineern.learning.Form.LostUsernameForm;
import jp.sysengineern.learning.Form.SignupForm;
import jp.sysengineern.learning.Repository.UsersRepository;
import jp.sysengineern.learning.ServiceImpl.UserSecurityService;
import jp.sysengineern.learning.Util.BaseURL;
import jp.sysengineern.learning.Util.MailTemplate;

@Controller
public class LoginController {
    //----ログイン、サインアップは失敗時に値を戻さない。----

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private MailTemplate mailTemplate;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BaseURL baseURL;

    //ログインページ表示メソッド
    @GetMapping("login")
    public String login() {
        return "userSecurity/login";
    }

    //ログインページポストメソッド
    @PostMapping("/login")
    public String loginPost() {
        return "redirect:/login-error";
    }

    //ログイン失敗時のリダイレクト先
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "userSecurity/login";
    }

    //ユーザー名を忘れた場合のページ表示メソッド
    @GetMapping("/inquiry_username")
    public String lostUsername(Model model) {
        model.addAttribute("lostUsernameForm",new LostUsernameForm());
        return "userSecurity/inquiryUserName";
    }

    //ユーザー名を忘れた場合のページからのポストメソッド
    @PostMapping("/inquiry_username")
    public String lostUsernamePost(Model model,@Validated LostUsernameForm lostUsernameForm,BindingResult result) {
        if(result.hasErrors()) {
            return "userSecurity/inquiryUserName";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users userByEmail = usersRepository.findByMailAddress(lostUsernameForm.getMailAddress());

        if(userByEmail == null) {
            model.addAttribute("fieldError",true);
            return "userSecurity/inquiryUserName";
        }

        if(passwordEncoder.matches(lostUsernameForm.getPassword(), userByEmail.getPassword())) {
            //メールテンプレートを取得　引数（DBに登録されたユーザー名、ユーザーが入力したメルアド）
            SimpleMailMessage mail = mailTemplate.inquiryUsername(userByEmail.getUsername(), userByEmail.getMailAddress());

            //メール送信
            this.mailSender.send(mail);
        }else {
            model.addAttribute("fieldError",true);
            return "userSecurity/inquiryUserName";
        }

        return "userSecurity/usernameInquiryCompleted";
    }

    //パスワードを忘れた場合のページ
    @GetMapping("/email_vrified_check_to_password_change")
    public String lostPassword(Model model) {
        model.addAttribute("lostPasswordForm",new LostPasswordForm());
        return "userSecurity/emailVerifiedCheckToChangePassword";
    }

    //パスワード忘れた場合のページからのポストメソッド
    @PostMapping("/email_vrified_check_to_password_change")
    public String lostPasswordPost(Model model,@Validated LostPasswordForm lostPasswordForm,BindingResult result) {
        if(result.hasErrors()) {
            model.addAttribute("lostPasswordForm",lostPasswordForm);
            return "userSecurity/emailVerifiedCheckToChangePassword";
        }

        Users user = usersRepository.findByMailAddress(lostPasswordForm.getMailAddress());
        if(user != null) {
          //if そのメールアドレスが登録されていたら

            String baseurl = baseURL.getUrl();
            String url = baseurl + "/mail_address_verified_check/change_password/" + user.getUuid();

            //メールテンプレートを取得　引数（DBに登録されたユーザー名、ユーザーが更新したメルアド、DBに登録されたuuid付きURL）
            SimpleMailMessage mail = mailTemplate.passwordReset(user.getUsername(), lostPasswordForm.getMailAddress(), url);

            //メール送信
            this.mailSender.send(mail);

            return "userSecurity/passwordChangeVerified";
        }
        else {

            model.addAttribute("emailError",true);
            model.addAttribute("lostPasswordForm",lostPasswordForm);
            return "userSecurity/emailVerifiedCheckToChangePassword";
        }
    }

    //メールアドレス到達性チェック
    @GetMapping("/mail_address_verified_check/change_password/{uuid}")
    public ModelAndView chgPass(ModelAndView mav,@PathVariable String uuid) {
        LostPasswordForm lostPasswordForm = new LostPasswordForm();
        mav.addObject("lostPasswordForm",lostPasswordForm);
        List<Users> isExist = usersRepository.findByUuidEquals(uuid);

        if(!isExist.isEmpty()) {
            mav.addObject("uuid",uuid);
            mav.setViewName("userSecurity/passwordChange");
        }else {
            mav.setViewName("userSecurity/passwordChangeFail1");
        }
        return mav;
    }

    //
    @PostMapping("/mail_address_verified_check/change_password")
    public String chngPassPost(Model model, @Validated LostPasswordForm lostPasswordForm,BindingResult result) {
        if(result.hasErrors()) {
            model.addAttribute("uuid",lostPasswordForm.getUuid());

            return "userSecurity/passwordChange";
        }
        Users user = usersRepository.findByUuid(lostPasswordForm.getUuid());
        model.addAttribute("uuid",lostPasswordForm.getUuid());

        if(lostPasswordForm.getPassword().equals(lostPasswordForm.getCheckPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String password = passwordEncoder.encode(lostPasswordForm.getPassword());
            user.setPassword(password);
            usersRepository.save(user);
        }else {
            model.addAttribute("error",true);


            return "userSecurity/passwordChange";
        }
        return "redirect:/password_change_completed";
    }

    @GetMapping("/password_change_completed")
    public String chngPassComp(Model model) {
        return "/userSecurity/passwordChangeCompleted";
    }

    //-------------------------------------------------------
    //サインアップページ表示
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm",new SignupForm());
        return "userSecurity/signup";
    }

    //サインアップメソッド
    //@ResponseBody
    @PostMapping("/signup")
    public String signupPost(Model model,@Validated SignupForm signupForm,
            BindingResult result,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "userSecurity/signup";
        }
        //パスワードと確認用パスワードが違ったら戻す
        if (!signupForm.getPassword().equals(signupForm.getCheckpassword())) {
            model.addAttribute("passwordError",true);
            return "userSecurity/signup";
        }

        try {
            //uuid付きのURLをStringで取得する
            String url = userSecurityService
                    .registerUser(signupForm.getUsername(),signupForm.getPassword(),signupForm.getMailAddress());

            //メールテンプレートを取得
            SimpleMailMessage mail = mailTemplate.template1(signupForm.getUsername(), signupForm.getMailAddress(), url);

            //メール送信
            this.mailSender.send(mail);

        }catch(DataIntegrityViolationException e) {
            model.addAttribute("signupError",true);
            return "userSecurity/signup";
        }

        redirectAttributes.addFlashAttribute("userMailAddress", signupForm.getMailAddress());
        return "redirect:/temporary_registration";
    }

    @GetMapping("/temporary_registration")
    public String tempRegist(Model model,@ModelAttribute("userMailAddress") String userMailAddress) {
        model.addAttribute("emailAddressCheckForm",new EmailAddressCheckForm());
        model.addAttribute("userMailAddress",userMailAddress);

        return "userSecurity/temporaryRegistration";
    }

    @PostMapping("/temporary_registration")
    public String tempResitPost(Model model,
            EmailAddressCheckForm emailAddressCheckForm,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userMailAddress", emailAddressCheckForm.getMailAddress());
        return "redirect:/email_address_check";
    }

    //メールアドレス到達性チェック
    @RequestMapping("/mailAddress_Verified_Check/{uuid}")
    public ModelAndView signupComp(ModelAndView mav,@PathVariable String uuid) {
        mav.setViewName("userSecurity/emailAddressVerifiedCheckedReturn");
        String checkedReturn = userSecurityService.mailAddressVerifiedCheck(uuid);
        mav.addObject("msg",checkedReturn);
        return mav;
    }

    //メール不到達時アドレス変更
    @GetMapping("/email_address_check")
    public String mailAddressCheck(Model model,@ModelAttribute("userMailAddress") String userMailAddress) {
        model.addAttribute("emailAddressCheckForm",new EmailAddressCheckForm());
        model.addAttribute("userMailAddress",userMailAddress);

        return "userSecurity/newEmailAddress";
    }

    //
    @PostMapping("/email_address_check")
    public String mailAddressCheckPost(Model model,
            @Validated EmailAddressCheckForm emailAddressCheckForm,BindingResult result,RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            model.addAttribute("userMailAddress", emailAddressCheckForm.getMailAddress());
            return "userSecurity/newEmailAddress";
        }
        Users user = usersRepository.findByMailAddress(emailAddressCheckForm.getMailAddress());

        String baseurl = baseURL.getUrl();
        //if あれば
        String url = baseurl + "/mailAddress_Verified_Check/" + user.getUuid();

        //メールテンプレートを取得　引数（DBに登録されたユーザー名、ユーザーが更新したメルアド、DBに登録されたuuid付きURL）
        SimpleMailMessage mail = mailTemplate.template1(user.getUsername(), emailAddressCheckForm.getUpdateMailAddress(), url);

        //メール送信
        this.mailSender.send(mail);

        redirectAttributes.addFlashAttribute("userMailAddress", emailAddressCheckForm.getUpdateMailAddress());
        return "redirect:/temporary_registration";
    }
}
