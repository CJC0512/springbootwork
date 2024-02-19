package com.ohgiraffers.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class FileUploaderController {

    /* 설명. build된 파일 업로드 할 경로를 가져오기 위해 ResourceLoader 의존성 주입 받기 */
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("single-file")
    public String singleFileUpload(@RequestParam MultipartFile singleFile,
                                   @RequestParam String singleFileDescription,
                                   RedirectAttributes rttr) throws IOException {

        /* 설명. encType = "multipart/form-data" 형태로 넘어온 파일은 MultipartFile 타입으로 받게 된다. */
        System.out.println("singleFile = " + singleFile);
        System.out.println("singleFileDescription = " + singleFileDescription);

        /* 설명. build 경로의 static에 있는 파일 업로드 할 곳의 경로를 받아온다. (미리 경로에 해당하는 디렉토리 생성 및 빌드 확인할 것) */
        Resource resource = resourceLoader.getResource("classpath:static/uploadFiles/img/single");
//        System.out.println("빌드 된 single 디렉토리의 절대경로: " + resource.getFile().getAbsolutePath());
        String filePath = resource.getFile().getAbsolutePath();

        /* 설명. 사용자가 넘긴 파일을 확인하고, rename해 보자.(우리는 자바의 UUID 클래스를 활용해서 rename 함) */
        String originFileName = singleFile.getOriginalFilename();
        System.out.println("originFileName = " + originFileName);

        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        System.out.println("ext = " + ext);

        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        System.out.println("savedName = " + savedName);

        /* 설명. 우리가 지정한 경로로 파일을 저장하고, */
        try {
            singleFile.transferTo(new File(filePath + "/" + savedName));

            /* 설명. DB를 다녀오는 BL(Business Logic) 구문 작성 (DB에 저장) */
            /* 설명. BL이 성공하면 화면의 재료를 RedirectAttributes로 담는다.(flashAttribute)*/
            rttr.addFlashAttribute("message", "파일 업로드 성공");
            rttr.addFlashAttribute("img", "uploadFiles/img/single/" + savedName);
            rttr.addFlashAttribute("singleFileDescription", singleFileDescription);
        } catch (IOException e) {

            /* 설명. try 구문 안에서(DB를 다녀오는 business logic 처리) 예외가 발생했을 때,
             *       파일만 올라가면 안되므로 찾아서 다시 지워줌. */
            new File(filePath + "/" + savedName).delete();
        }

        return "redirect:/result";

    }

    @PostMapping("multi-file")
    public String multiFileUpload(@RequestParam List<MultipartFile> multiFiles,
                                  @RequestParam String multiFileDescription,
                                  RedirectAttributes rttr) throws IOException {
//        System.out.println("multiFiles = " + multiFiles);
//        System.out.println("multiFileDescription = " + multiFileDescription);

        String filePath = resourceLoader.getResource("classpath:static/uploadFiles/img/multi")
                                        .getFile().getAbsolutePath();

        /* 설명. 사용자가 올린 파일들을 다 rename하고 저장하는 작업 및 DB로 전달하기 위한 List에 쌓기 */
        List<Map<String, String>> files = new ArrayList<>();        // DB에 저장할 값들을 지닌 List
        List<String> saveFiles = new ArrayList<>();                 // 화면단에서 img 태그가 참조할 정적 Resource 경로(src 경로)

        try {
            for (int i = 0; i < multiFiles.size(); i++) {
                String originFileName = multiFiles.get(i).getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                /* 설명. DB에 저장할 사용자가 올린 하나의 파일 정보 (DB 테이블 컬럼을 참고할 것) */
                Map<String, String> file = new HashMap<>();
                file.put("originFileName", originFileName);
                file.put("savedName", savedName);
                file.put("filePath", filePath);
                file.put("multiFileDescription", multiFileDescription);

                files.add(file);

                multiFiles.get(i).transferTo(new File(filePath + "/" + savedName));
                saveFiles.add("uploadFiles/img/multi/" + savedName);
            }

            /* 설명. 여기까지 문제가 없었다면 화면에 재료를 던져준다. */
            rttr.addFlashAttribute("message", "다중 파일 업로드 성공!");
            rttr.addFlashAttribute("imgs", saveFiles);
            rttr.addFlashAttribute("multiFileDescription", multiFileDescription);
        } catch(Exception e){

            /* 설명. List<Map<String, String>> files로 쌓인 업로드 된 파일들을 찾아 일일이 다시 지운다.*/
            for(int i = 0; i < files.size(); i++){
                Map<String, String> file = files.get(i);
                new File(filePath + "/" + file.get("savedName")).delete();
            }

            /* 설명. 실패 했다면 실패 메시지를 전달한다.*/
            rttr.addFlashAttribute("message", "파일 업로드 실패!");
        }
        return "redirect:/result";
    }

    @GetMapping("result")
    public void result() {
    }
}
