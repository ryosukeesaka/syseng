package jp.sysengineern.learning.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class SimpleErrorController implements ErrorController {

    @Override
    public String getErrorPath() {

        return "/error";
    }


    @RequestMapping
    public ModelAndView error(HttpServletRequest httpServletRequest,ModelAndView mav) {
        mav.setStatus(HttpStatus.NOT_FOUND);

        mav.setViewName("errorPage/error");
        return mav;
    }
}
