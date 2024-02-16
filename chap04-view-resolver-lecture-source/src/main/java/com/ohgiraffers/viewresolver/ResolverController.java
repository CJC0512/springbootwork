package com.ohgiraffers.viewresolver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/*")
public class ResolverController {

    /* 설명. 서블릿 때와 마찬가지로 포워딩을 통해 값을 전달할 수 있다. */
    @GetMapping("string")
    public String stringReturning(Model model){
        model.addAttribute("forwardMessage", "문자열로 뷰 이름 반환함...");
        return "result";
    }

    /* 설명. 서블릿 때와 마찬가지로 파라미터를 활용하지 않고서는 리다이렉트를 통해 값ㅇ르 전달할 수 없다. (스프링은 해법이 있다.) */
    @GetMapping("string-redirect")
    public String stringRedirect(Model model){
        model.addAttribute("message1", "문자열로 뷰 이름 반환하며 리다이렉트");

        return "redirect:/";
//        return "redirect:/?message1=ttt";   // parameter식으로 주었기에 html에서 param.message1을 통해 해당 값을 사용할 수 있음.
    }

    /* 설명. 스프링의 RedirectAttributes라는 객체에 attr을 담으면 리다이렉트 시에도 값(전달하고자 하는 상태값)이 유지된다. */
    @GetMapping("string-redirect-attr")
    public String stringRedirectFlashAttribute(RedirectAttributes rttr){
        rttr.addFlashAttribute("flashMessage1", "리다이렉트 attr 사용하여 redirect...");
        return "redirect:/";
    }

    /* 설명. 기존에 핸들러 메소드가 void 또는 String으로만 반환했는데 ModelAndView를 반환하는 것도 해 보자. */
    /* 설명. 단순 forward 시에는 String 반환과 Model을 활용하는 코드와 ModelAndView를 활용한 코드가 차이가 없다.*/
    @GetMapping("modelandview")
    public ModelAndView modelAndViewTest(ModelAndView mv){
        mv.addObject("message2", "ModelAndView를 이용한 forward");
        mv.setViewName("result");       // 이동 경로(페이지) 지정

        return mv;
    }

}
