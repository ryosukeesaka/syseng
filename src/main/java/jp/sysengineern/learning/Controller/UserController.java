package jp.sysengineern.learning.Controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.sysengineern.learning.Entity.UserDetail;
import jp.sysengineern.learning.Entity.Users;
import jp.sysengineern.learning.Form.FollowFollowerForm;
import jp.sysengineern.learning.Form.MyPageUserEditForm;
import jp.sysengineern.learning.Repository.UserDetailRepository;
import jp.sysengineern.learning.Service.UserService;
import jp.sysengineern.learning.Util.BaseURL;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private BaseURL baseURL;

    // 他ユーザーページ表示
    @RequestMapping(value = "/user/yourpage/other_profile")
    public ModelAndView otherProfile(ModelAndView mav, @RequestParam Long userId, Principal principal) {
        Users otherUser = userService.findByUserId(userId);
        mav.addObject("otherUser", otherUser);
        List<Users> otherFollowing = otherUser.getFollowing();
        mav.addObject("otherFollowing", otherFollowing);
        List<Users> otherFollowers = otherUser.getFollowers();
        mav.addObject("otherFollowers", otherFollowers);
        //他者をfollowしていればフォローを外すボタン、していなければの切り替えに使用
        Users user = userService.findByUsername(principal.getName());
        List<Users> following = user.getFollowing();
        mav.addObject("following", following);
        FollowFollowerForm followFollowerForm = new FollowFollowerForm();
        mav.addObject("followFollowerForm", followFollowerForm);
        mav.setViewName("yourPage/otherProfile");
        return mav;
    }

    // 他ユーザーページのアンフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/other_profile", params = "unfollow", method = RequestMethod.POST)
    public ModelAndView otherUnfollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal,
            RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Long otherUserId = followFollowerForm.getUserId();
        Users otherUser = userService.findByUserId(otherUserId);
        loginUser.getFollowing().remove(otherUser);
        redirectAttributes.addAttribute("userId", otherUserId);
        return new ModelAndView("redirect:/user/yourpage/other_profile");
    }

    // 他ユーザーページのフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/other_profile", params = "follow", method = RequestMethod.POST)
    public ModelAndView otherFollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal,
            RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Long otherUserId = followFollowerForm.getUserId();
        Users otherUser = userService.findByUserId(otherUserId);
        loginUser.getFollowing().add(otherUser);
        redirectAttributes.addAttribute("userId", otherUserId);
        return new ModelAndView("redirect:/user/yourpage/other_profile");
    }

    // 他ユーザーのフォローページ表示
    @RequestMapping(value = "/user/yourpage/follow", method = RequestMethod.GET)
    public ModelAndView otherFollow(
            Principal principal,
            ModelAndView mav,
            Pageable pageable,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam Long userId) {
        Users otherUser = userService.findByUserId(userId);
        mav.addObject("otherUser", otherUser);

        List<Users> otherFollowing = otherUser.getFollowing();
        PagedListHolder<Users> pages = new PagedListHolder<Users>(otherFollowing);
        if (page == null) {
            page = 0;
        }
        pages.setPageSize(3);
        pages.setPage(page);
        mav.addObject("otherFollowing", pages.getPageList());
        mav.addObject("next", pages.getPage() + 1);
        mav.addObject("last", pages.isLastPage());
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        List<Users> following = user.getFollowing();
        mav.addObject("following", following);
        FollowFollowerForm followFollowerForm = new FollowFollowerForm();
        mav.addObject("followFollowerForm", followFollowerForm);
        mav.setViewName("YourPage/otherFollow");
        return mav;
    }

    // 他ユーザーフォローページのフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/follow", params = "follow", method = RequestMethod.POST)
    public ModelAndView otherFollowFollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal,
            RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users otherFollowUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().add(otherFollowUser);
        return new ModelAndView("redirect:/user/yourpage/follow?userId=" + followFollowerForm.getOtherUserId());
    }

    // 他ユーザーフォローページのアンフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/follow", params = "unfollow", method = RequestMethod.POST)
    public ModelAndView otherFollowUnfollow(ModelAndView mav, FollowFollowerForm followFollowerForm,
            Principal principal, RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users otherFollowUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().remove(otherFollowUser);
        return new ModelAndView("redirect:/user/yourpage/follow?userId=" + followFollowerForm.getOtherUserId());
    }

    // 他ユーザーのフォロワーページ表示
    @RequestMapping(value = "/user/yourpage/follower", method = RequestMethod.GET)
    public ModelAndView otherFollower(
            Principal principal,
            ModelAndView mav,
            Pageable pageable,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam Long userId) {
        Users otherUser = userService.findByUserId(userId);
        mav.addObject("otherUser", otherUser);
        List<Users> otherFollowers = otherUser.getFollowers();
        PagedListHolder<Users> pages = new PagedListHolder<Users>(otherFollowers);
        if (page == null) {
            page = 0;
        }
        pages.setPageSize(15);
        pages.setPage(page);
        mav.addObject("otherFollowers", pages.getPageList());
        mav.addObject("next", pages.getPage() + 1);
        mav.addObject("last", pages.isLastPage());
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        List<Users> following = user.getFollowing();
        mav.addObject("following", following);
        FollowFollowerForm followFollowerForm = new FollowFollowerForm();
        mav.addObject("followFollowerForm", followFollowerForm);
        mav.setViewName("YourPage/otherFollower");
        return mav;
    }

    // 他ユーザーフォロワーページのフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/follower", params = "follow", method = RequestMethod.POST)
    public ModelAndView otherFollowerFollow(ModelAndView mav, FollowFollowerForm followFollowerForm,
            Principal principal, RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users otherFollowerUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().add(otherFollowerUser);
        return new ModelAndView("redirect:/user/yourpage/follower?userId=" + followFollowerForm.getOtherUserId());
    }

    // 他ユーザーフォロワーページのアンフォローボタン
    @Transactional
    @RequestMapping(value = "/user/yourpage/follower", params = "unfollow", method = RequestMethod.POST)
    public ModelAndView otherFollowerUnfollow(ModelAndView mav, FollowFollowerForm followFollowerForm,
            Principal principal, RedirectAttributes redirectAttributes) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users otherFollowerUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().remove(otherFollowerUser);
        return new ModelAndView("redirect:/user/yourpage/follower?userId=" + followFollowerForm.getOtherUserId());
    }

    // プロフィールページ表示
    @RequestMapping("/user/mypage/profile")
    public ModelAndView mypage(ModelAndView mav, Principal principal) {
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        List<Users> following = user.getFollowing();
        mav.addObject("following", following);
        List<Users> followers = user.getFollowers();
        mav.addObject("followers", followers);
        mav.setViewName("myPage/profile");
    	
    	
    	
    	
    	URL url = new URL("http://dokojava.jp");
    	InputStream is = url.openStream();
    	InputStreamReader isr = new InputStreamReader(is);
    	int ix = isr.read();
    	while(ix != -1) {
    		System.out.println((char)ix);
    		ix = isr.read();
    	}
    	isr.close();

    	Socket sock = new Socket("dokojava.jp",80);
    	InputStream is3 = sock.getInputStream();
    	OutputStream os3 = sock.getOutputStream();
    	//int i3 = is3.read();
    	os3.write("GET /index.html HTTP/1.0\r\n".getBytes());
    	os3.write("\r\n".getBytes());
    	os3.flush();
    	InputStreamReader isr2 = new InputStreamReader(is3);
    	int i3 = isr2.read();
    	while(i3 != -1) {
    		System.out.println((char)i3);
    		i3 = isr2.read();
    	}
    	sock.close();
    	/*FileOutputStream fos = new FileOutputStream("c:\\Work\\tin.txt",true);
    	fos.write(20);
    	fos.flush();
    	fos.close();
    	FileWriter fw = new FileWriter("c:\\Work\\tin.txt",true);
    	fw.write("unko boke kasu");
    	fw.flush();
    	fw.close();
    	FileReader fr = new FileReader("c:\\Work\\tin.txt");
    	int in = fr.read();
    	while(in != -1) {
    		char c = (char) in;
    		System.out.println(c);
    		in = fr.read();
    	}
    	fr.close();*/
    	//FileWriter ff = null;
    	try(FileWriter ff = new FileWriter("c:\\Work\\tin.txt");){
    		ff.write("あ、どもども");
        	ff.flush();
    	}catch(IOException e) {
    		System.out.println("エラーですよーーーー");
    	}

    	String tel01 = "012-3456-78900";
        String tel02 = "012-3456-789a";
        String patternx = "^\\d{2,4}-\\d{2,4}-\\d{4}$";
        Pattern ppp = Pattern.compile(patternx);
        if (ppp.matcher(tel01).find()) {
          System.out.println(tel01 + "は電話番号です");
        } else {
          System.out.println(tel01 + "は電話番号ではありません");
        }
    	String str3 = "012-3456-7890";
    	Pattern op = Pattern.compile("^¥d{2,4}-¥d{2,4}-¥d{4}$");
    	if(op.matcher(str3).find()){
            System.out.println(str3 + "はabで始まります");
        }else{
            System.out.println(str3 + "はabで始まりません");
        }

    	 String sst = "abc0000" ;
    	 String sst2 = "090909";
         String pattern = "[a-z]{3}\\d" ;
         String pattern2 = "\\d{4}";
         Pattern pa = Pattern.compile(pattern);
         Pattern pa2 = Pattern.compile(pattern2);
         if(pa.matcher(sst).find()){
             System.out.println(sst + "はabで始まります");
         }else{
             System.out.println(sst + "はabで始まりません");
         }
         //Pattern pa2 = Pattern.compile(pattern2);
         if(pa2.matcher(sst2).find()){
             System.out.println(sst2 + "はabで始まります");
         }else{
             System.out.println(sst2 + "はabで始まりません");
         }

         String sss = "abc,huj:uho";
         String[] words = sss.split("[,:]");
         for(String sc : words) {
        	 System.out.println(sc + "->");
        	 System.out.println(words[0]);
         }

    	//圧縮対象のデータ(今回は文字列)
        /*String source = "これはテストです。\n以上。";
        //圧縮後のファイルの保存場所
        String outputFilePath = "c:\\Work\\output.txt.gz";
        //圧縮前にバイト配列に置き換える際のエンコーディング
        String encoding = "UTF-8";

        GZIPOutputStream gout = null;

        try {
            gout = new GZIPOutputStream(new FileOutputStream(outputFilePath));
            gout.write(source.getBytes(encoding));
        }
        finally {
            if (gout != null) {
                try {
                    gout.close();
                }
                catch (IOException e) {
                }
            }
        }*/




    	FileInputStream fis = null;
    	GZIPOutputStream zip = null;
    	String out = "c:\\Work\\tin.txt";
    	try {
    	fis = new FileInputStream("c:\\Work\\tin.txt");
    	FileOutputStream fos = new FileOutputStream("c:\\Work\\kuso.txt");
    	BufferedOutputStream bos = new BufferedOutputStream(fos);
    	 zip = new GZIPOutputStream(bos);
    	//Files.copy(Paths.get("c:\\Work\\tin.txt"),Paths.get("c:\\Work\\kusoo.txt"));
    	int b = fis.read();
    	while(b != -1) {
    		zip.write(b);
    		b = fis.read();
    	}
    	zip.flush();
    	zip.close();
    	fis.close();
    	System.out.println(zip + "おーーーーーーーーーーい");
    	}catch(IOException e) {
    		try {
    			if(fis != null) {
    				fis.close();
    			}
    			if(zip != null) {
    				zip.close();
    			}
    			System.out.println("ぶーーーーーーーーーーい");
    		}catch(IOException ee) {
    			System.out.println("おうううーーーーーーーい");
    		}
    	}
    	StringReader sr = null;
    	try {
    		String msg = "うんこ　ぼけ　かす";
    		sr = new StringReader(msg);

    		while(true) {
        		int i = sr.read();
        		if(i == -1) {
        			break;
        		}
        		System.out.println( (char)i );
    		}
    	} catch( IOException e )
        {
            // 読み込みに失敗した際に、write()メソッドが
            // IOException例外を投げます。
            e.printStackTrace();
        }
        finally
        {
            // 最後にclose()メソッドを呼んで後処理をします。
            // また、これは必ず行うため、finally内で行います。
            if( sr != null )
            {
                sr.close();
            }
        }

    	Users u = new Users("unkoman","x@x");
    	FileOutputStream fos2 = new FileOutputStream("c:\\Work\\tinko.txt");
    	ObjectOutputStream oos = new ObjectOutputStream(fos2);
    	oos.writeObject(u);
    	oos.flush();
    	oos.close();
    	FileInputStream fis2 = new FileInputStream("c:\\Work\\tinko.txt");
    	ObjectInputStream ois = new ObjectInputStream(fis2);
    	Users user2 = (Users)ois.readObject();
    	ois.close();
    	System.out.println(user2 + "＼(^o^)／");
    	Users u2 = new Users("unkoman","x@x");
    	if(u.equals(u2) == true){
    		System.out.println("gooooooooooooood");
    		}else{
    		    System.out.println("boooooooood");
    		}
    	File file = new File("c:\\Work\\tin.txt");
    	//ファイルパスを取得する
        String str = file.getAbsolutePath();
        System.out.println("pass : " + str);
    	Path path = Paths.get("c:\\Work\\tin.txt");
    	Path path2 = Paths.get("c:\\Work\\kuso.txt");
    	long l = Files.size(path2);
    	System.out.println(l + "kusoooooooooooooooooo");
    	try {
            // ファイルを作成する
            Files.createFile(path2);
        } catch (AccessDeniedException e) {
            // ファイルを作成するディレクトリにアクセス権限がない場合
            System.out.println(e);
        } catch (FileAlreadyExistsException e) {
            // ファイルがすでに作成されている場合
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    	String p = path.toAbsolutePath().toString();

        System.out.println("pass : " + p);
    	File f = path.toFile();


    	Log logger = LogFactory.getLog(Users.class);
    	System.out.println("本日は\n晴天なり------------------------");
    	System.out.println(logger);
    	ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "echo", "zzz");
    	pb.start();
    	System.out.println(System.getProperty("java.version"));
    	System.out.println("本日は\n晴天なり");
    	int[] o = {1,2,3};
    	IntStream m = Arrays.stream(o);
    	System.out.println(m);
    	List<String> lists = Arrays.asList("C:\\Work\\LD_KAISHA_MASTER\\KAISHA");
    	lists.forEach(list -> System.out.println(list));
    	Users h1 = new Users("tin","001","tin@tin","akajs");
    	System.out.println(h1);
        String name = principal.getName();
        System.out.println("----------------------------------------");
        Users user = userService.findByUsername(name);
        //String ss = user.getUserDetail().getUserDescription();
        mav.addObject("user", user);
        System.out.println("!----------------------------------------");
        List<Users> following = user.getFollowing();
        following.add(h1);
        //List<Users> u= new	ArrayList<Users>();
        h1 = new Users("tink","001","tin@tin","akajs");
        Writer w = new BufferedWriter(new FileWriter("C:\\Work\\unchi.txt"));
        w.write(user.getUsername());
        w.flush();
        w.close();
        following.remove(h1);
        Long lo = 10L;
        System.out.println(h1);
        System.out.println(following);
        //Collections.sort(following);
        //System.out.println(following);
        mav.addObject("following", following);
        System.out.println("----------------------------------------!");
        List<Users> followers = user.getFollowers();
        mav.addObject("followers", followers);
        mav.setViewName("myPage/profile");

        StrongBox<Integer> k = new StrongBox<Integer>(KyeType.BUTTON);
        //E i = StrongBox.get();
        k.put(1);
        Integer i = k.get();

        System.out.println(i);

      //圧縮対象のデータ(今回は文字列)
        String source = "これはテストです。\n以上。";
        //圧縮後のファイルの保存場所
        String outputFilePath = "c:\\Work\\output.txt.gz";
        //圧縮前にバイト配列に置き換える際のエンコーディング
        String encoding = "UTF-8";

        GZIPOutputStream gout = null;

        try {
            gout = new GZIPOutputStream(new FileOutputStream(outputFilePath));
            gout.write(source.getBytes(encoding));
        }
        finally {
            if (gout != null) {
                try {
                    gout.close();
                }
                catch (IOException e) {
                }
            }
        }

        String unko = "うんこ、ぼけ、かす、なす";
        StringTokenizer st = new StringTokenizer(unko,",");
        while(st.hasMoreTokens()) {
        	String tin = st.nextToken();
        	System.out.println(tin);
        }

        Writer xx = new FileWriter("c:\\Work\\output.properties");
        Properties pro = new Properties();
        pro.setProperty("heroName","うんこ");
        pro.setProperty("heroHp","100");
        pro.store(xx, "勇者の情報");
        xx.close();

        Reader fr = new FileReader("c:\\Work\\output.properties");
        Properties pp = new Properties();
        pp.load(fr);
        String name1 = pp.getProperty("heroName");
        String hp = pp.getProperty("heroHp");
        int hhp = Integer.parseInt(hp);
        System.out.println(name1 + hhp + "でおおおおおおお");

  
        return mav;
    }

    // プロフィール編集ページ表示
    @RequestMapping("/user/mypage/profileedit")
    public ModelAndView profileEdit(ModelAndView mav, Principal principal) {
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        MyPageUserEditForm myPageUserEditForm = new MyPageUserEditForm();
        mav.addObject("myPageUserEditForm", myPageUserEditForm);
        mav.setViewName("myPage/profileEdit");
        return mav;
    }

    // ユーザー情報を編集する
    @RequestMapping(value = "/user/mypage/profileedit", params = "profileEdit", method = RequestMethod.POST)
    public ModelAndView profileEditPost(ModelAndView mav, Principal principal, MyPageUserEditForm myPageUserEditForm)
            throws Exception {
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        UserDetail userDetail = user.getUserDetail();

        File originalFileName = new File(myPageUserEditForm.getUploadFile().getOriginalFilename());
        //File renamedFileName = new File("user_image_" + user.getUserId() + ".jpg");

        File renamedFileName = null;

        if (!myPageUserEditForm.getUploadFile().getOriginalFilename().equals("")) {
            //String uploadPath = "src/main/resources/static/image/upload/";
        	//文字列に含まれる文字を大文字に変換した新しい文字列を返す。
            String fileName_UpperCase = myPageUserEditForm.getUploadFile().getOriginalFilename().toUpperCase();

            String uploadPath = baseURL.getImgUploadPath();
            String uploadPath_backUp = baseURL.getImgUploadPathBackUp();
            byte[] bytes = myPageUserEditForm.getUploadFile().getBytes();

            String jpgFile = "user_image_" + user.getUserId() + ".jpg";
            String jpegFile = "user_image_" + user.getUserId() + ".jpeg";
            String pngFile = "user_image_" + user.getUserId() + ".png";
            String gifFile = "user_image_" + user.getUserId() + ".gif";
            //ファイル名が一意になるようにユーザー名をファイル名に

            if(fileName_UpperCase.endsWith(".JPG")
                    || fileName_UpperCase.endsWith(".JPEG")
                    || fileName_UpperCase.endsWith(".PNG")
                    || fileName_UpperCase.endsWith(".GIF")) {
                if(fileName_UpperCase.endsWith(".JPG")) {
                    renamedFileName = new File(jpgFile);
                }else if(fileName_UpperCase.endsWith(".JPEG")) {
                    renamedFileName = new File(jpegFile);
                }else if(fileName_UpperCase.endsWith(".PNG")) {
                    renamedFileName = new File(pngFile);
                }else if(fileName_UpperCase.endsWith(".GIF")) {
                    renamedFileName = new File(gifFile);
                }
            }else {
                mav.addObject("user", user);

                mav.addObject("myPageUserEditForm", myPageUserEditForm);
                mav.setViewName("myPage/profileEdit");
                mav.addObject("fileTypeError", true);
                return mav;
            }
            originalFileName.renameTo(renamedFileName);

            //ImageIOクラス 画像をファイルから読み込んだり、指定したファイルに書き込むクラスメソッドが存在
            //BufferedImageクラスのオブジェクトとして読み込むことができるようにする。
            //ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));//バイト配列から1バイトずつ読み込む
            if (bi == null) {
                mav.addObject("user", user);

                mav.addObject("myPageUserEditForm", myPageUserEditForm);
                mav.setViewName("myPage/profileEdit");
                mav.addObject("fileTypeError", true);
                return mav;
            }
            //BufferedOutputStream バイトようのフィルタ
            //if(new File(uploadPath + ("user_image_" + user.getUserId())
            //FileOutputStreamバイト配列で1バイトずつ順次書き込んでいく FileInputstreamが読み込み
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(uploadPath + renamedFileName)));//バイナリファイルに書き込み
            stream.write(bytes);
            stream.close();

            BufferedOutputStream stream_backUp = new BufferedOutputStream(
                    new FileOutputStream(new File(uploadPath_backUp + renamedFileName)));
            stream_backUp.write(bytes);
            stream_backUp.close();
            //toStringメソッドでバイト配列が得られる
            String strRenamedFileName = renamedFileName.toString();
            userDetail.setImgPath(strRenamedFileName);
        }
        userDetail.setUserDisplayName(myPageUserEditForm.getUserDisplayName());
        userDetail.setUserShortDescription(myPageUserEditForm.getUserShortDescription());
        userDetail.setUserDescription(myPageUserEditForm.getUserDescription());
        userDetailRepository.save(userDetail);
        return new ModelAndView("redirect:/user/mypage/profile");
    }

    // フォローページ表示
    @RequestMapping(value = "/user/mypage/follow", method = RequestMethod.GET)
    public ModelAndView follow(
            Principal principal,
            ModelAndView mav,
            Pageable pageable,
            @RequestParam(name = "page", required = false) Integer page) {
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        List<Users> following = user.getFollowing();

        PagedListHolder<Users> pages = new PagedListHolder<Users>(following);

        if (page == null) {
            page = 0;
        }
        pages.setPageSize(15);
        pages.setPage(page);

        mav.addObject("following", pages.getPageList());
        mav.addObject("next", pages.getPage() + 1);
        mav.addObject("last", pages.isLastPage());
        FollowFollowerForm followFollowerForm = new FollowFollowerForm();
        mav.addObject("followFollowerForm", followFollowerForm);
        mav.setViewName("myPage/follow");
        return mav;
    }

    // フォローページからダイレクトメッセージページへ遷移
    @RequestMapping(value = "/user/mypage/follow", params = "directMessage", method = RequestMethod.POST)
    public ModelAndView followToDirectMessage(ModelAndView mav, FollowFollowerForm followFollowerForm,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("recieverUserId", followFollowerForm.getUserId());
        return new ModelAndView("redirect:/user/mypage/direct_message");
    }

    // フォローページのアンフォローボタン
    @Transactional
    @RequestMapping(value = "/user/mypage/follow", params = "unfollow", method = RequestMethod.POST)
    public ModelAndView followUnfollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users followUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().remove(followUser);
        return new ModelAndView("redirect:/user/mypage/follow");
    }

    // フォロワーページ表示
    @RequestMapping(value = "/user/mypage/follower", method = RequestMethod.GET)
    public ModelAndView follower(
            Principal principal,
            ModelAndView mav,
            Pageable pageable,
            @RequestParam(name = "page", required = false) Integer page) {
        String name = principal.getName();
        Users user = userService.findByUsername(name);
        mav.addObject("user", user);
        List<Users> followers = user.getFollowers();

        PagedListHolder<Users> pages = new PagedListHolder<Users>(followers);

        if (page == null) {
            page = 0;
        }
        pages.setPageSize(15);
        pages.setPage(page);

        mav.addObject("followers", pages.getPageList());
        mav.addObject("next", pages.getPage() + 1);
        mav.addObject("last", pages.isLastPage());
        List<Users> following = user.getFollowing();
        mav.addObject("following", following);
        FollowFollowerForm followFollowerForm = new FollowFollowerForm();
        mav.addObject("followFollowerForm", followFollowerForm);
        mav.setViewName("myPage/follower");
        return mav;
    }

    // フォロワーページのアンフォローボタン
    @Transactional
    @RequestMapping(value = "/user/mypage/follower", params = "unfollow2", method = RequestMethod.POST)
    public ModelAndView followerUnfollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users followerUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().remove(followerUser);
        return new ModelAndView("redirect:/user/mypage/follower");
    }

    // フォロワーページのフォローボタン
    @Transactional
    @RequestMapping(value = "/user/mypage/follower", params = "follow", method = RequestMethod.POST)
    public ModelAndView followerFollow(ModelAndView mav, FollowFollowerForm followFollowerForm, Principal principal) {
        Users loginUser = userService.findByUsername(principal.getName());
        Users followerUser = userService.findByUserId(followFollowerForm.getUserId());
        loginUser.getFollowing().add(followerUser);
        return new ModelAndView("redirect:/user/mypage/follower");
    }

    // フォロワーーページからダイレクトメッセージページへ遷移
    @RequestMapping(value = "/user/mypage/follower", params = "directMessage", method = RequestMethod.POST)
    public ModelAndView followerToDirectMessage(ModelAndView mav, FollowFollowerForm followFollowerForm,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("recieverUserId", followFollowerForm.getUserId());
        return new ModelAndView("redirect:/user/mypage/direct_message");
    }

}
