package com.ohgiraffers.handlermethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller

/* 설명. 현재의 Controller 클래스에 작성할 핸들러 메소드들이 모두 /first/xxx의 요청을 받게 될 때
 *       클래스에 어노테이션을 추가할 수 있다.
 * */
@RequestMapping("/first")

/* 설명. 이 Controller 클래스의 핸들러 메소드에서 Model에 'id'라는 키 값으로 담는 값들은 Session에 담으라는 어노테이션 */
@SessionAttributes("id")
public class FirstController {

    /* 설명. 반환형이 void인 핸들러 메소드는 요청 경로 자체가 view의 경로 및 이름을 반환한 것으로 바로 해석이 된다. */
    @GetMapping("regist") //"/regist" 혹은 "regist" 둘다 허용
//    public String regist() {
//
//        /* 설명. 핸들러 메소드에서 반환하는 String 값은 templates에 있는 view(html 파일)의 이름(경로)이다.*/
//        return "/first/regist";
//    }
    public void regist() {
    }


    /* 설명. request를 쓰게 된다면 request 개념은 사용자의 입력값이 존재, model은 동적 페이지의 재료를 담는 용도로 쓰자 */
    @PostMapping("regist")
//    public String registMenu(HttpServletRequest request){
    public String registMenu(WebRequest request, Model model) {
        String name = request.getParameter("name");
        System.out.println("name = " + name);

        int price = Integer.valueOf(request.getParameter("price"));
        int categoryCode = Integer.valueOf(request.getParameter("categoryCode"));

        String message = name + "을(를) 신규 메뉴 목록의 " + categoryCode + "번 카테고리에 "
                + price + "원으로 등록하였습니다!!!";
        model.addAttribute("message", message);

        return "/first/messagePrinter";
    }

    @GetMapping("modify")
    public void modify() {
    }

    /* 설명.
     *   Request의 parameter로 넘어오는 값들의 key값과 일치하는 변수명을 작성하고
     *   @RequestParam 어노테이션을 적용하면 알아서 값을 꺼내고 해당 매개변수의 자료형에 맞게 변환까지 해 준다.
     *   @RequestParam 어노테이션은 생략도 가능하다.
     *   (=> 스프링 부터는 컨트롤러의 파싱(Parsing) 작업이 간소화 된다는 것을 알 수 있다.)
     * */

    /* 설명.
     *   @RequestParam의 속성들
     *   1. defaultValue: 사용자의 입력 값이 없거나("") 아니면 request의 parameter 키 값과 일치하지 않는
     *                    매개변수 명 사용 시 매개변수가 가질 기본 default값을 작성한다.
     *   2. name: request parameter의 키 값과 다른 매개변수 명을 사용하고 싶을 때, request parameter의
     *            키 값을 작성한다.
     * */
    @PostMapping("modify")
    public String modifyMenu(Model model,
                             @RequestParam(defaultValue = "디폴트", name = "name") String modifyName
            , @RequestParam(defaultValue = "0") int modifyPrice) {
        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @PostMapping("modify2")
    public String modifyMenu(Model model, @RequestParam Map<String, String> parameter) {

        String modifyName = parameter.get("name2");
        int modifyPrice = Integer.valueOf(parameter.get("modifyPrice2"));

        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @GetMapping("search")
    public void searchMenu() {
    }

    /* 설명. 핸들러 메소드에 우리가 작성한 클래스를 매개변수로 작성하면 스프링이 객체를 만들어 주고
     *    setter로 값도 주입해 준다.(커맨드 객체)
     * */
    /* 설명. @ModelAttribute 어노테이션을 활용하면
     *   커맨드 객체를 모델에도 담아주며 어트리뷰트의 키 값을 지정할 수 있다. (키 값이 없으면 타입의 낙타봉 표기법이 키갑이다.)
     * */
    @PostMapping("search")
    public String searchMenu(@ModelAttribute("menu") MenuDTO menu) {
        System.out.println("menu = " + menu);
        return "first/searchResult";
    }

    @GetMapping("login")
    public void login() {
    }

    @PostMapping("login")
    public String sessionTest1(HttpSession session, @RequestParam String id) {
        session.setAttribute("id", id);
        return "first/loginResult";
    }

    @GetMapping("logout1")
    public String logoutTest1(HttpSession session) {
        session.invalidate();
        return "first/loginResult";
    }

    @PostMapping("login2")
    public String sessionTest2(Model model, @RequestParam String id) {
        model.addAttribute("id", id);

        return "first/loginResult";
    }

    /* 설명. @sessionAttribute 방식으로 session에 담긴 값은 SessionStatus에서 제공하는 setComplete()로 만료 시켜야 햔댜. */
    @GetMapping("logout2")
    public String logoutTest2(SessionStatus sessionStatus){
        sessionStatus.setComplete();

        return "first/loginResult";
    }

    @GetMapping("body")
    public void body(){

    }

    @PostMapping("body")
    public void body(@RequestBody String body, @RequestHeader("content-type") String contentType,
                     @CookieValue(value="JSESSIONID") String sessionId){
        System.out.println("body = " + body);
        System.out.println("contentType = " + contentType);
        System.out.println("sessionId = " + sessionId);
    }
}
