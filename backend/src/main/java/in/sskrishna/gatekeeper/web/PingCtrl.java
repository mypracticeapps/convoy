package in.sskrishna.gatekeeper.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class PingCtrl {

    @GetMapping("/ping")
    public void ping() {

    }
}
