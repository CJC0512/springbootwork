package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    private List<UserDTO> users;

    public ResponseEntityTestController() {
        this.users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date()));
    }

    /* 설명. 1. 직접 ResponseEntity 객체 만들기 */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers(){
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
//        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }

    /* 설명. 2. 빌더를 활용한 메소드 체이닝 방식으로 ResponseEntity 객체 만들기 (요즘 유행) */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo){

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        /* 설명. 요청 리소스(회원 번호와 일치하는 회원 한명)을 추출 */
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200,"조회 성공!", responseMap));
    }
    
    /* 설명.
    *   기본에 우리가 배웠던 @RequestParam과 달리 json 문자열이 핸들러 메소드로 넘어올 때는 @RequestBody를 붙이고
    *   받아내야 한다. json 문자열의 각 프로퍼티 별로 받을 수도 있지만, 한번에 하나의 타입으로 받아낼 때는 커맨드 객체(UserDTO)를
    *   활용해야 하며, 커맨드 객체는 json 문자열의 프로퍼티명과 일치해야 한다.
    *
    * 설명.
    *   {
    *       "id" : "user01"
    *   }
    *   이 때 "id" : "user01"을 프로퍼티, "id"를 '프로퍼티명'이라고 한다.
    * */
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser){
        System.out.println("newUser = " + newUser);

        /* 설명. auto-increment 개념을 컬렉션 마지막에 있는 회원 번호 + 1로 해서 newUser에 추가하기 */
        int lastUserNo = users.get(users.size() - 1).getNo();       // 컬렉션에 쌓인 마지막 회원의 회원 번호
        newUser.setNo(lastUserNo + 1);                              // 마지막 회원 번호 +1을 newUser에 set

        users.add(newUser);

        return ResponseEntity
                .created(URI.create("/entity/users/" + users.get(users.size() - 1).getNo()))
                .build();
    }

    @PutMapping("users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo){

        /* 설명. PathVariable로 넘어온 번호와 일치하는 회원 한명 추출 */
        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList())
                        .get(0);

        /* 설명. 사용자가 넘겨준 수정하고자 하는 데이터로 회원 정보를 수정 */
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity
                .created(URI.create("entity/users/" + userNo))
                .build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo){

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList())
                        .get(0);

        /* 설명. ArrayList에서 제공하는 remove는 인덱스 대신 자신이 관리하는 객체를 찾아서 지워 주기도 한다. */
        users.remove(foundUser);

        return ResponseEntity
                .noContent()        // 204
                .build();
    }
}
