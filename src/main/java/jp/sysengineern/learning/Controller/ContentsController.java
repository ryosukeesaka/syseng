package jp.sysengineern.learning.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.sysengineern.learning.Entity.CourceItem;
import jp.sysengineern.learning.Repository.CourceItemRepository;

@Controller
public class ContentsController {

    @Autowired
    private CourceItemRepository courceItemRepository;

    // 表示テスト用 ==== コンテンツサンプル表示 ==== 最後に消す
    @RequestMapping(value = "/contents")
    public ModelAndView cource(ModelAndView mav) {
        mav.setViewName("learning/contents");
        return mav;
    }
    // 表示テスト用 ==== コンテンツサンプル表示 ==== 最後に消す,ここまで


    //コンテンツ表示用
    @RequestMapping(value = "/user/cource_{url}/detail_{title_url}/item_{item_url}")
    public ModelAndView contentItem(ModelAndView mav,
            @PathVariable("url") String url,
            @PathVariable("title_url") String title_url,
            @PathVariable("item_url") String item_url) {
        CourceItem courceItem = courceItemRepository.findByUrl(item_url);
        mav.setViewName("learning/" + url + "/" + title_url + "/" + item_url);
        mav.addObject("courceItem", courceItem);
        return mav;
    }
}