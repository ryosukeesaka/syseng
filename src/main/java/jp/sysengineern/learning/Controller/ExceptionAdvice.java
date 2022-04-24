package jp.sysengineern.learning.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.terasoluna.gfw.web.token.transaction.InvalidTransactionTokenException;

import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.MyPageUserEditForm;
import jp.sysengineern.learning.Service.UserService;

@ControllerAdvice
public class ExceptionAdvice {

    @Autowired
    private UserService userService;

    //Controller個別で例外をハンドリングする場合
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView maxUploadSizeException(MaxUploadSizeExceededException e,Principal principal) {
        ModelAndView mav = new ModelAndView();
        //Principal principal = new Principal();
        MyPageUserEditForm myPageUserEditForm = new MyPageUserEditForm();

        String name = principal.getName();
        Users user = userService.findByUsername(name);

        mav.addObject("user", user);

        mav.addObject("myPageUserEditForm", myPageUserEditForm);
        mav.setViewName("myPage/profileEdit");
        mav.addObject("fileSizeError", true);
        return mav;
    }

    @ExceptionHandler(InvalidTransactionTokenException.class)
    public ModelAndView invalidTransactionTokenException(InvalidTransactionTokenException e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errorPage/doubleTransactionAvoid");
        return mav;
    }
}
