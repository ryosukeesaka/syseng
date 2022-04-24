package jp.sysengineern.learning.Controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.sysengineern.learning.Entity.Community;
import jp.sysengineern.learning.Entity.Message;
import jp.sysengineern.learning.Entity.Reply;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.CommunityForm;
import jp.sysengineern.learning.Form.FollowFollowerForm;
import jp.sysengineern.learning.Form.MessageDeleteForm;
import jp.sysengineern.learning.Form.MessageForm;
import jp.sysengineern.learning.Form.ReplyDeleteForm;
import jp.sysengineern.learning.Form.ReplyForm;
import jp.sysengineern.learning.Repository.CommunityRepository;
import jp.sysengineern.learning.Repository.MessageRepository;
import jp.sysengineern.learning.Repository.ReplyRepository;
import jp.sysengineern.learning.Repository.UsersRepository;
import jp.sysengineern.learning.Service.UserService;

@Controller
public class CommunityController {

	@Autowired
	private CommunityRepository communityRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ReplyRepository replyRepository;
	@Autowired
	private UsersRepository usersRepository;

	//コミュニティ詳細
	@RequestMapping(value="/user/community_detail",method=RequestMethod.GET)
	public ModelAndView community(
			Principal principal,
			@ModelAttribute("replyPosted") String replyPosted,
			ModelAndView mav,
			Pageable pageable) {

		//現状コミュニティが１つのみなのでIDを直で挿入している
		Long communityId = 1l;
		Community community = communityRepository.findByCommunityId(communityId);
		mav.addObject("community", community);

		Page<Message> messageList = messageRepository.findByCommunity(community,pageable);
		mav.addObject("messageList", messageList);


		Users loginUser = userService.findByUsername(principal.getName());
		mav.addObject("loginUser", loginUser);

		MessageForm messageForm = new MessageForm();
		mav.addObject("messageForm", messageForm);

		int next = pageable.next().getPageNumber();
		mav.addObject("next",next);
		mav.addObject("last",messageList.isLast());
		mav.addObject("page",pageable.getPageNumber());
		List<Users> userList = usersRepository.findByCommunity(community);
		mav.addObject("userList",userList);

		Long longReplyPosted = 0L;//初期値
		if(!replyPosted.equals("")) {
	    //@ModelAttributeはLong型の値を入力することができない？、ようなのでStringにて入力してLongに変換して使用する
	    longReplyPosted = Long.valueOf(replyPosted);
	        }
		mav.addObject("longReplyPosted", longReplyPosted);

		mav.setViewName("community/communityDetail");
		return mav;
	}

	//コミュティ内メッセージの送信
	@RequestMapping(value="/user/community_detail",params = "messagePost",method=RequestMethod.POST)
	public ModelAndView post(
			Principal principal,
			@ModelAttribute MessageForm messageForm,
			@ModelAttribute CommunityForm communityForm,
			ModelAndView mav,
			Pageable pageable) {
		//ユーザー情報の取得
		Users loginUser = userService.findByUsername(principal.getName());
		mav.addObject("loginUser", loginUser);
		//コミュニティを取得
		Community community = communityRepository.findByCommunityId(messageForm.getCommunity_community_id());
		mav.addObject("community", community);
		//メッセージを取得
		mav.addObject("messageForm", messageForm);
		Message message = new Message(messageForm.getMessageBody(),loginUser,community);
		messageRepository.save(message);
		List<Users> userList = usersRepository.findByCommunity(community);
        mav.addObject("userList",userList);

		mav.addObject("message",message);

		//無限スクロール
		Page<Message> messageList = messageRepository.findByCommunity(community,pageable);
		mav.addObject("messageList", messageList);
		int next = pageable.next().getPageNumber();
        mav.addObject("next",next);
        mav.addObject("last",messageList.isLast());
        mav.addObject("page",pageable.getPageNumber());

		mav.addObject("communityForm", communityForm);

		mav.setViewName("community/communityDetail");
		return mav;
	}

	//メッセージの削除
    @Transactional//dbとやりとりする際に必要？？
    @PostMapping(value="/user/community_detail",params="messageDelete")
    public ModelAndView messageDelet(@ModelAttribute MessageDeleteForm messageDeleteForm,Principal principal,RedirectAttributes redirectAttributes ) {
        messageRepository.deleteByMessageId(messageDeleteForm.getMessageId());

        return new ModelAndView("redirect:/user/community_detail");
    }

	//返信機能
	@RequestMapping(value="/user/community_detail",params= "reply" ,method=RequestMethod.POST)
	public ModelAndView reply(
			ModelAndView mav,
			@ModelAttribute ReplyForm replyForm,
			@ModelAttribute MessageForm messageForm,
			RedirectAttributes redirectAttributes,
			Principal principal){
		Message message = messageRepository.findByMessageId(replyForm.getMessageId());
		Users loginUser = userService.findByUsername(principal.getName());
		Reply reply = new Reply(replyForm.getReplyBody(),message,loginUser);
		replyRepository.save(reply);
		mav.addObject("reply", reply);
		mav.setViewName("community/communityDetail");
		redirectAttributes.addFlashAttribute("replyPosted",replyForm.getMessageId());
		return new ModelAndView("redirect:/user/community_detail");
	}

	//返信の削除
    @Transactional
    @PostMapping(value="/user/community_detail",params="deleteReply")
    public ModelAndView replyDelet(@ModelAttribute ReplyDeleteForm replyDeleteForm,Principal principal,RedirectAttributes redirectAttributes ) {
        replyRepository.deleteByReplyId(replyDeleteForm.getReplyId());
        redirectAttributes.addFlashAttribute("replyPosted",replyDeleteForm.getMessageId());

        return new ModelAndView("redirect:/user/community_detail");
    }

	//コミュニティ参加ボタン
	@Transactional
	@RequestMapping(value="/user/community_detail",params= "join" ,method=RequestMethod.POST)
	public ModelAndView communityJoin(
			ModelAndView mav,
			@ModelAttribute CommunityForm communityForm,
			Principal principal){
		Users loginUser = userService.findByUsername(principal.getName());
		Community community = communityRepository.findByCommunityId(communityForm.getCommunityId());
		loginUser.getCommunity().add(community);
		return new ModelAndView("redirect:/user/community_detail");
	}

	//コミュニティ退会
	@Transactional
	@RequestMapping(value="/user/community_detail",params= "withdrawal" ,method=RequestMethod.POST)
	public ModelAndView communityWithdrawal(
			ModelAndView mav,
			@ModelAttribute CommunityForm communityForm,
			Principal principal){
		Users loginUser = userService.findByUsername(principal.getName());
		Community community = communityRepository.findByCommunityId(communityForm.getCommunityId());
		loginUser.getCommunity().remove(community);
		return new ModelAndView("redirect:/user/community_detail");
	}

	//IDを持ってメンバーリストページへの遷移
	@RequestMapping(value="/user/community_detail",params= "link" ,method=RequestMethod.POST)
	public ModelAndView link(
			ModelAndView mav,
			RedirectAttributes redirectAttributes,
			@ModelAttribute CommunityForm communityForm,
			Principal principal){

		Long communityId = communityForm.getCommunityId();
		redirectAttributes.addAttribute("communityId", communityId);
		return new ModelAndView("redirect:/user/community_detail/member_list");
	}

	// コミュニティメンバーリストの表示
	@RequestMapping(value="/user/community_detail/member_list",method=RequestMethod.GET)
	public ModelAndView memberList(
			Principal principal,
			@ModelAttribute CommunityForm communityForm,
			@RequestParam("communityId")  Long communityId,
			Model model,
			ModelAndView mav,
			Pageable pageable,
			@RequestParam(name="page",required = false) Integer page){

		Community community = communityRepository.findByCommunityId(communityId);
		mav.addObject("community", community);

		List<Users> userList = usersRepository.findByCommunity(community);
		mav.addObject("memberCount",userList);
		Users loginUser = userService.findByUsername(principal.getName());
		mav.addObject("loginUser", loginUser);
		//リストの先頭にログインユーザーを表示させるために削除
		userList.remove(loginUser);

		PagedListHolder<Users> pages = new PagedListHolder<Users>(userList);
		if(page == null) {
			page = 0;
		}
		pages.setPageSize(15);
		pages.setPage(page);

		mav.addObject("userList",pages.getPageList());
		mav.addObject("next",pages.getPage()+1);
		mav.addObject("last",pages.isLastPage());

		List<Users> following = loginUser.getFollowing();
		mav.addObject("following", following);
		FollowFollowerForm followFollowerForm = new FollowFollowerForm();
		mav.addObject("followFollowerForm", followFollowerForm);
		mav.addObject("communityId",communityId);
		mav.setViewName("community/communityDetailMember");
		return mav;
	}

	//メンバーリストのフォローボタン
	@Transactional
	@RequestMapping(value = "/user/community_detail/member_list", params = "follow", method = RequestMethod.POST)
	public ModelAndView followerFollow(
			ModelAndView mav,
			@ModelAttribute FollowFollowerForm followFollowerForm,
			Principal principal,
			RedirectAttributes redirectAttributes
			) {
		Users loginUser = userService.findByUsername(principal.getName());
		Users followerUser = userService.findByUserId(followFollowerForm.getUserId());
		loginUser.getFollowing().add(followerUser);
		mav.addObject("loginUser", loginUser);
		Long communityId = followFollowerForm.getCommunityId();
		redirectAttributes.addAttribute("communityId", communityId);
		return new ModelAndView("redirect:/user/community_detail/member_list");
	}
	//メンバーリストのアンフォローボタン
	@Transactional
	@RequestMapping(value = "/user/community_detail/member_list", params = "unfollow", method = RequestMethod.POST)
	public ModelAndView followUnfollow(
			ModelAndView mav,
			@ModelAttribute FollowFollowerForm followFollowerForm,
			Principal principal,
			RedirectAttributes redirectAttributes) {
		Users loginUser = userService.findByUsername(principal.getName());
		Users followUser = userService.findByUserId(followFollowerForm.getUserId());
		loginUser.getFollowing().remove(followUser);
		Long communityId = followFollowerForm.getCommunityId();
		redirectAttributes.addAttribute("communityId", communityId);
		return new ModelAndView("redirect:/user/community_detail/member_list");
	}

}