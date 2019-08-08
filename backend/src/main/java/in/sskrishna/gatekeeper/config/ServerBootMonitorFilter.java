package in.sskrishna.gatekeeper.config;

import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class ServerBootMonitorFilter implements Filter {
    private final String wu[] = new String[]{

    };
    private final RestErrorBuilder errorBuilder;

    public ServerBootMonitorFilter(RestErrorBuilder errorBuilder) {
        this.errorBuilder = errorBuilder;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI().toString();

        if (uri.startsWith("/api")) {
            if (GlobalLockRepo.isLocked(GlobalLockRepo.KEYS.SERVER_BOOTING)) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(421);
            } else {
                chain.doFilter(request, response);
                return;
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
    }
}
