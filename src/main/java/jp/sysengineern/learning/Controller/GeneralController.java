package jp.sysengineern.learning.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneralController {


	@RequestMapping("/user/layout/baselayout")
	public ModelAndView menu(ModelAndView mav) {
		mav.setViewName("layout/baseLayout");
		return mav;
	}

	@RequestMapping("/user/layout/mypagelayout")
	public ModelAndView sidebar(ModelAndView mav) {
		mav.setViewName("layout/myPageLayout");
		return mav;
	}

	@RequestMapping("/user/top")
	public ModelAndView top(ModelAndView mav) {
	    mav.setViewName("test");
	    return mav;
	}

	@RequestMapping("/")
	public ModelAndView test(ModelAndView mav,Principal principal) {

	    mav.setViewName("general/index");
	    return mav;
	}

	@RequestMapping("/kiyaku")
	public ModelAndView kiyaku(ModelAndView mav) {
	    mav.setViewName("general/kiyaku");
	    return mav;
	}

	@RequestMapping("/privacy")
    public ModelAndView privacy(ModelAndView mav) {
        mav.setViewName("general/privacyPolicy");
        return mav;
    }
}
