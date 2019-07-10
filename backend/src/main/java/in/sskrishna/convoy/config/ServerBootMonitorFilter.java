package in.sskrishna.convoy.config;

import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class ServerBootMonitorFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (GlobalLockRepo.isLocked(GlobalLockRepo.KEYS.SERVER_BOOTING)) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(307);
        } else {
            chain.doFilter(request, response);
        }
    }
    // other methods 
}