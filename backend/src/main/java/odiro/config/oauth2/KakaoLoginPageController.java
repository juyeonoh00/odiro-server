//package odiro.config.oauth2;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//// 테스트 페이지
//@Controller
//@Slf4j
//@RequestMapping("/login")
//public class KakaoLoginPageController {
//
//    @GetMapping("/page")
//    public String loginPage(Model model) {
//        String client_id = "041c2f4f97b6949e3d0d99a52a15ce33";
//        String redirect_uri = "http://localhost:8080/kakao";
//
//        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+ client_id +"&redirect_uri="+ redirect_uri;
//        model.addAttribute("location", location);
//        log.info(location);
//        return "login";
//    }
//}