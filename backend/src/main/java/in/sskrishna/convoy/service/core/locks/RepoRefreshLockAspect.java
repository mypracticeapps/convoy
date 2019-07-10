package in.sskrishna.convoy.service.core.locks;

import in.sskrishna.convoy.exception.InProgressException;
import in.sskrishna.convoy.model.GitRepo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class RepoRefreshLockAspect {

    @Around("@annotation(in.sskrishna.convoy.service.core.locks.RepoRefreshLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.getArgs()[0];
        String repoId = GlobalLockRepo.KEYS.REPO_REFRESH + ":";
        if (obj instanceof String) {
            repoId += obj.toString();
        } else if (obj instanceof GitRepo) {
            GitRepo repo = (GitRepo) obj;
            repoId += repo.getId();
        }
        
        try {
            if (GlobalLockRepo.lock(repoId)) {
                return joinPoint.proceed();
            } else {
                throw new InProgressException();
            }
        } finally {
            GlobalLockRepo.unlock(repoId);
        }
    }
}