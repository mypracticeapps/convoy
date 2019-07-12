package in.sskrishna.convoy.config;

import io.sskrishna.rest.response.ErrorCodeLookup;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class RestResponseConfig {

    @Bean
    public ErrorCodeLookup errorCodeLookup() throws IOException {
        Resource resource = new ClassPathResource("/error_codes.yaml");
        ErrorCodeLookup errorCodeLookup = new ErrorCodeLookup(resource);
        return errorCodeLookup;
    }

    @Bean
    public RestErrorBuilder restErrorBuilder(ErrorCodeLookup errorCodeLookup) {
        return new RestErrorBuilder(errorCodeLookup);
    }
}