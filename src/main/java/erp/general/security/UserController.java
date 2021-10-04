package erp.general.security;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public String login() {
        return "---------welcome------------";
    }

}
