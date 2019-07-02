package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.BranchSet;
import in.sskrishna.convoy.repository.BranchesRepository;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class BranchCtrl {
    private final BranchesRepository branchRepository;

    public BranchCtrl(BranchesRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("/branches")
    public BranchSet getBranches(@RequestParam("repoId") String repoId) {
        return this.branchRepository.findOne(repoId);
    }
}