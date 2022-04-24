package jp.sysengineern.learning.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import jp.sysengineern.learning.Entity.Complete;
import jp.sysengineern.learning.Entity.Cource;
import jp.sysengineern.learning.Entity.CourceDetail;
import jp.sysengineern.learning.Entity.CourceItem;
import jp.sysengineern.learning.Entity.CourceMessage;
import jp.sysengineern.learning.Entity.CourceReply;
import jp.sysengineern.learning.Entity.CourceTitle;
import jp.sysengineern.learning.Entity.CourceTitleList;
import jp.sysengineern.learning.Entity.Progress;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.CourceMessageDeleteForm;
import jp.sysengineern.learning.Form.CourceMessageForm;
import jp.sysengineern.learning.Form.CourceReplyDeleteForm;
import jp.sysengineern.learning.Form.CourceReplyForm;
import jp.sysengineern.learning.Repository.CourceDetailRepository;
import jp.sysengineern.learning.Repository.CourceItemRepository;
import jp.sysengineern.learning.Repository.CourceMessageRepository;
import jp.sysengineern.learning.Repository.CourceReplyRepository;
import jp.sysengineern.learning.Repository.CourceRepository;
import jp.sysengineern.learning.Repository.CourceTitleRepository;
import jp.sysengineern.learning.Repository.UsersRepository;

@Controller
public class LearningController {

	@Autowired
	private CourceRepository courceRepository;
	@Autowired
	private CourceDetailRepository courceDetailRepository;
	@Autowired
	private CourceTitleRepository courceTitleRepository;
	@Autowired
	private CourceItemRepository courceItemRepository;
	@Autowired
	private CourceMessageRepository courceMessageRepository;
	@Autowired
	private CourceReplyRepository courceReplyRepository;
	@Autowired
	private UsersRepository usersRepository;

	//コース一覧
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		List<Cource> courceList = courceRepository.findAll();

		mav.addObject("courceList", courceList);
		mav.setViewName("learning/loginIndex");
		return mav;
	}

	//各コースタイトル一覧
	@RequestMapping(value = "/user/cource_{url}", method = RequestMethod.GET)
	public ModelAndView courceTitleList(ModelAndView mav, Principal principal, @PathVariable String url) {

	    Users user = usersRepository.findByUsername(principal.getName());
	    List<Complete> comp = user.getComplete();
	    //Long id = comp.get(0).getCompleteId(); リストから要素を１つ取得
	    List<Progress> progress = user.getProgress();
	    List<Long> compIdList = new ArrayList<Long>();
	    for(Complete c : comp) {
	        compIdList.add(Long.parseLong(c.getCompleteItemId()));
	    }

		Cource cource = courceRepository.findByUrl(url);
		List<CourceTitle> courceTitle = cource.getCourceTitle();
		List<CourceTitleList> titleList = new ArrayList<CourceTitleList>();

		for (CourceTitle elem : courceTitle) {
		    double itemCount = 0;
		    double userCompleteCount = 0;
		    double complete = 0;

		    List<CourceDetail> courceDetailList = elem.getCourceDetail();//
		    CourceItem courceItem = courceItemRepository.findTop1ByCourceDetail(courceDetailList.get(0));
		    CourceItem progressCourceItem = new CourceItem();
		    Long progressId = 0L;
		    for(int j=0;j<progress.size();j++) {
		    	if(elem.getCourceTitleId().equals(progress.get(j).getCourceTitleId())) {
		    		progressId = progress.get(j).getProgress() + 1L;
		    		progressCourceItem = courceItemRepository.findByCourceItemId(progressId);
		    	} else {
		    		progressId = 0L;
		    	}
		    }

		    for(int i=0;i<courceDetailList.size();i++) {
		        itemCount += courceDetailList.get(i).getCourceItem().size();
		        List<CourceItem> itemList = courceDetailList.get(i).getCourceItem();
		        for (CourceItem item: itemList) {
		            if(compIdList.contains(item.getCourceItemId())) {
		                userCompleteCount += 1;
		            };
		        }
		    }
		   if(userCompleteCount == 0.0 || itemCount == 0.0) {
		       complete = 0;
		   }else {
		       complete = (userCompleteCount/itemCount)*100.0;
		   }


		    CourceTitleList l = new CourceTitleList(
		            elem.getCourceTitleId(),
		            elem.getCourceTitleName(),
		            elem.getCourceTitleDescription(),
		            elem.getUrl(),
		            elem.getCourceDetail(),
		            itemCount,
		            userCompleteCount,
		            complete,
		            courceItem,
		            progressCourceItem
		            );
		    titleList.add(l);

		}
		mav.addObject("titleList",titleList);
		mav.addObject("cource", cource);
		mav.setViewName("learning/courceTitleList");
		return mav;
	}

	//各コース詳細　　title(vba基礎1)=>detail(vbaとは？、vbaに触れる)
	@RequestMapping(value = "/user/cource_{url}/detail_{title_url}", method = RequestMethod.GET)
	public ModelAndView courceDetailsList(ModelAndView mav,Principal principal, @PathVariable("url") String url,
			@PathVariable("title_url") String title_url) {
		CourceTitle courceTitle = courceTitleRepository.findByUrl(title_url);
		List<CourceDetail> courceDetailList = courceDetailRepository.findByCourceTitle(courceTitle);
	    Users user = usersRepository.findByUsername(principal.getName());
	    List<Complete> comp = user.getComplete();
		mav.addObject("courceTitle", courceTitle);
		mav.addObject("url",url);
		mav.addObject("courceDetailList",courceDetailList);
		mav.addObject("comp",comp);
		mav.setViewName("learning/courceDetailList");
		return mav;
	}

	//
	@TransactionTokenCheck(value = "first", type = TransactionTokenType.BEGIN)
	@RequestMapping(value = "/user/cource_{url}/items_{detail_url}", method = RequestMethod.GET)
	public ModelAndView courceItemsList(ModelAndView mav, Principal principal, @PathVariable("url") String url,
			@PathVariable("detail_url") String detail_url, @ModelAttribute("redirected") String redirected,
			@ModelAttribute("replyPosted") String replyPosted) {
		Users user = usersRepository.findByUsername(principal.getName());
		CourceDetail courceDetail = courceDetailRepository.findByUrl(detail_url);
		List<CourceItem> courceItem = courceItemRepository.findByCourceDetail(courceDetail);

		List<CourceMessage> courceMessageList = courceMessageRepository.findByCourceDetail(courceDetail);

		mav.addObject("courceDetail", courceDetail);
		mav.addObject("courceItem", courceItem);

		mav.addObject("user", user);
		mav.addObject("url", url);
		mav.addObject("courceMessageList", courceMessageList);
		Long longReplyPosted = 0L;
		if (!replyPosted.equals("")) {
			//@ModelAttributeはLong型の値を入力することができない？、ようなのでStringにて入力してLongに変換して使用する
			longReplyPosted = Long.valueOf(replyPosted);
		}

		mav.addObject("redirected", redirected);
		mav.addObject("longReplyPosted", longReplyPosted);
		mav.setViewName("learning/courceItemsList");

		return mav;
	}

	@GetMapping("/user/courceReplyPost/{userId}/{messageId}")
	public ModelAndView courceReply(ModelAndView mav, @PathVariable("userId") Long userId,
			@PathVariable("messageId") Long messageId) {

		CourceMessage courceMessage = courceMessageRepository.findByMessageId(messageId);

		mav.addObject("courceMessage", courceMessage);

		mav.setViewName("learning/courceMessageReplyForm");

		return mav;
	}

	@PostMapping(value = "/user", params = "courceReplyPost")
	public String courceReplyPost(Model model, Principal principal, @ModelAttribute CourceReplyForm courceReplyForm,
			RedirectAttributes redirectAttributes) {
		Users user = usersRepository.findByUsername(principal.getName());
		CourceDetail courceDetail = courceDetailRepository.findByCourceDetailId(courceReplyForm.getCourceDetailId());
		CourceMessage courceMessage = courceMessageRepository.findByMessageId(courceReplyForm.getCourceMessageId());

		CourceReply courceReply = new CourceReply(user, courceReplyForm.getCourceReply(), courceMessage);
		String redirect = "redirect:/user/cource_" + courceReplyForm.getCourceUrl() + "/items_" + courceDetail.getUrl();
		courceReplyRepository.save(courceReply);
		redirectAttributes.addFlashAttribute("redirected", 1);
		redirectAttributes.addFlashAttribute("replyPosted", courceReplyForm.getCourceMessageId());
		return redirect;
	}

	@TransactionTokenCheck(value = "first", type = TransactionTokenType.IN)
	@PostMapping(value = "/user", params = "messagePost")
	public String postCourceMessage(Model model, Principal principal,
			@ModelAttribute CourceMessageForm courceMessageForm, RedirectAttributes redirectAttributes) {

		Users user = usersRepository.findByUsername(principal.getName());
		CourceDetail courceDetail = courceDetailRepository.findByCourceDetailId(courceMessageForm.getCourceDetailId());
		CourceMessage courceMessage = new CourceMessage(user, courceMessageForm.getMessageBody(), courceDetail);

		courceMessageRepository.save(courceMessage);
		redirectAttributes.addFlashAttribute("redirected", 1);

		String redirect = "redirect:/user/cource_" + courceMessageForm.getCourceUrl() + "/items_"
				+ courceDetail.getUrl();

		return redirect;
	}

	@Transactional
	@PostMapping(value = "/user", params = "messageDelete")
	public String messageDelet(@ModelAttribute CourceMessageDeleteForm courceMessageDeleteForm, Principal principal,
			RedirectAttributes redirectAttributes) {
		courceMessageRepository.deleteByMessageId(courceMessageDeleteForm.getMessageId());
		redirectAttributes.addFlashAttribute("redirected", 1);

		String redirect = "redirect:/user/cource_" + courceMessageDeleteForm.getCourceUrl() + "/items_"
				+ courceMessageDeleteForm.getCourceDetailUrl();
		return redirect;
	}

	@Transactional
	@PostMapping(value = "/user", params = "replyDelete")
	public String replyDelet(@ModelAttribute CourceReplyDeleteForm courceReplyDeleteForm, Principal principal,
			RedirectAttributes redirectAttributes) {
		courceReplyRepository.deleteByReplyId(courceReplyDeleteForm.getReplyId());
		redirectAttributes.addFlashAttribute("redirected", 1);
		redirectAttributes.addFlashAttribute("replyPosted", courceReplyDeleteForm.getCourceMessageId());
		String redirect = "redirect:/user/cource_" + courceReplyDeleteForm.getCourceUrl() + "/items_"
				+ courceReplyDeleteForm.getCourceDetailUrl();
		return redirect;
	}
}
