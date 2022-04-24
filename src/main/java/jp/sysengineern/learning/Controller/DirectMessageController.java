package jp.sysengineern.learning.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.sysengineern.learning.Entity.DirectMessage;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.DirectMessageForm;
import jp.sysengineern.learning.Repository.DirectMessageRepository;
import jp.sysengineern.learning.Repository.UsersRepository;

@Controller
public class DirectMessageController {

	@Autowired
	private DirectMessageRepository directMessageRepository;

	@Autowired
	private UsersRepository usersRepository;



	// ダイレクトメッセージページ表示
	@RequestMapping(value = "/user/mypage/direct_message")
	public ModelAndView directMessage(ModelAndView mav, Principal principal,Pageable pageable,
			@RequestParam("recieverUserId") Long recieverUserId,
			@RequestParam(name = "page",required = false) String page2) {
		//required デフォルトではパラメータ必須。任意にする場合、required属性にfalseを指定
		DirectMessageForm directMessageForm = new DirectMessageForm();
		mav.addObject("directMessageForm", directMessageForm);
		Users user = usersRepository.findByUsername(principal.getName());
		List<Users> allUser = usersRepository.findAll();
		List<Users> userHistory = new ArrayList<Users>();
		List<DirectMessage> msgList = new ArrayList<DirectMessage>();
		for (int i = 0; allUser.size() > i; i++) {
			//ユーザーが保有している全メッセージ取得
			List<DirectMessage> temp = allUser.get(i).getDirectMessage();
			msgList.addAll(temp);//引数として渡されたCollectionオブジェクトのすべてのアイテムをリストに追加します。
		}
		for (int m = 0; m < msgList.size(); m++) {//要素数
			for (int k = 0; k < msgList.size() - 1; k++) {//要素数ー１入れ替え
				//左の値が大きい場合には正の値、右の値の方が大きければ負の値
				if (msgList.get(k).getCreatedAt().compareTo(msgList.get(k + 1).getCreatedAt()) == -1) {
					DirectMessage temp = msgList.get(k + 1);
					//★セットメソッドー＞値の書き換え。List.set(場所,書き換える値);セットクラスとは別物
					msgList.set(k + 1, msgList.get(k));
					msgList.set(k, temp);
				}
			}
		}
		for (int j = 0; msgList.size() > j; j++) {
			//？番目のメッセージにおいて自分が送信者でユーザーヒストリーに受信者ユーザーが未追加場合、追加する
			if (msgList.get(j).getSenderUser().equals(user)) {
				if (!userHistory.contains(msgList.get(j).getRecieverUser())) {
					userHistory.add(msgList.get(j).getRecieverUser());
				}
			}
			if (msgList.get(j).getRecieverUser().equals(user)) {
				if (!userHistory.contains(msgList.get(j).getSenderUser())) {
					userHistory.add(msgList.get(j).getSenderUser());
				}
			}
		}
		mav.addObject("userHistory", userHistory);

		if (recieverUserId == 0) {
			// サイドメニューから遷移した場合
			List<Users> following = user.getFollowing();
			mav.addObject("following", following);

		} else {
			// 送信相手を選択済みの場合
			Users recieverUser = usersRepository.findByUserId(recieverUserId);
			List<Users> users = new ArrayList<Users>();
			users.add(user);
			users.add(recieverUser);
			List<DirectMessage> allMessage = directMessageRepository.findBySenderUserInAndRecieverUserInOrderByCreatedAtAsc(users,users);

			//ページ数
			int page = 0;

			if(allMessage.size() == 0) {
				page = 0 ;
			} else if(allMessage.size()%15 == 0) {

				//ページ数は0から始まる
				page = allMessage.size()/15 - 1;
				System.out.println(allMessage.size()+"-------------------------///////////");
				System.out.println(page+"★★★★★");
			} else {
				System.out.println("-------------------09090900---------------------");
				page = allMessage.size()/15;
				System.out.println(allMessage.size()+"-------------------------///////////");
				System.out.println(page+"★★★★★");
			}

			if(page2!= null) {
				if(Integer.parseInt(page2) != page){
					page = Integer.parseInt(page2);
					System.out.println(page+"▲▲▲▲");
				}
			}else {
				System.out.println(page2+"▲▲★★××");
			}


			//ページ番号とページサイズ
			Pageable pageable2 = PageRequest.of(page, 15);


			Page<DirectMessage> directMessage = directMessageRepository
					.findBySenderUserInAndRecieverUserInOrderByCreatedAtAsc(users,
							users, pageable2);
			Pageable pageablePrev = null;
			if(!directMessage.isEmpty()) {
			    if(directMessage.isLast()) {
			    	//総ページが1ページしか無い場合
	                if(directMessage.isFirst()) {
	                    pageablePrev = null;
	                }else {
	                    pageablePrev = PageRequest.of(page-1, 15);
	                	System.out.println("(*´Д｀)(*´Д｀)");
	                	System.out.println(pageablePrev+"(*´Д｀)(*´Д｀)");
	                }
	            }else {
	                pageablePrev = PageRequest.of(page, 15);
	                System.out.println(pageablePrev+"いいいいい(*´Д｀)(*´Д｀)");
	                System.out.println("いいいい(*´Д｀)(*´Д｀)");
	            }
			}



			Page<DirectMessage> directMessagePrev = directMessageRepository.findBySenderUserInAndRecieverUserInOrderByCreatedAtAsc(users, users, pageablePrev);

			int prev = 0;
			boolean first = false;

			if(directMessage.isLast()) {
			    if(directMessage.isFirst()) {
			        mav.addObject("isLast",false);
			        first = true;
			    }else if(directMessagePrev.isFirst()) {
			        mav.addObject("isLast",true);
			        first = true;

			    }else {
			        mav.addObject("isLast",true);
			        prev = pageable2.getPageNumber()-2;
			        if(page == 0) {
                        first = true;
                    }
			    }

			}else {
                prev = pageable2.getPageNumber()-1;
                if(page == 0) {
                    first = true;
                }
            }


//			if(directMessage.isLast()) {
//			    if(!directMessage.isFirst()) {
//			        mav.addObject("isLast",true);
//			    }
//
//			    prev = pageable2.getPageNumber()-2;
//
//
//			}else {
//			    prev = pageable2.getPageNumber()-1;
//			}

			if(prev < 0) {
				prev = 0;
			}


			mav.addObject("directMessage", directMessage); // メッセージ本文表示
			mav.addObject("directMessagePrev",directMessagePrev);
			mav.addObject("first",first);
			mav.addObject("recieverUserId", recieverUserId); // フォームのバリュー用
			mav.addObject("recieverUser", recieverUser); // 相手アイコン表示のため
			mav.addObject("msgBox", true);
			mav.addObject("selectedUser", true);
			mav.addObject("prev",prev);
		}
		mav.setViewName("myPage/directMessage");
		return mav;
	}

	// ダイレクトメッセージ送信
	@RequestMapping(value = "/user/mypage/direct_message", params = "sendMessage", method = RequestMethod.POST)
	public ModelAndView sendMessage(ModelAndView mav, DirectMessageForm directMessageForm, Principal principal,
			RedirectAttributes redirectAttributes) {
		Long recieverUserId = directMessageForm.getRecieverUserId();
		Users user = usersRepository.findByUsername(principal.getName());
		Users recieverUser = usersRepository.findByUserId(recieverUserId);
		DirectMessage directMessage = new DirectMessage(directMessageForm.getDirectMessageBody(), user, recieverUser);
		directMessageRepository.save(directMessage);
		redirectAttributes.addAttribute("recieverUserId", recieverUserId);
		return new ModelAndView("redirect:/user/mypage/direct_message");
	}

}
