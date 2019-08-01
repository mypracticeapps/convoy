package in.sskrishna.gatekeeper.web;

import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/debug/v1")
public class LockCtrl {
    @GetMapping("/lock")
    public boolean lock(@RequestParam("key") String key) {
        return GlobalLockRepo.lock(key);
    }

    @GetMapping("/unlock")
    public void unlock(@RequestParam("key") String key) {
        GlobalLockRepo.unlock(key);
    }

    @GetMapping("/islocked")
    public boolean islocked(@RequestParam("key") String key) {
        return GlobalLockRepo.isLocked(key);
    }
}
