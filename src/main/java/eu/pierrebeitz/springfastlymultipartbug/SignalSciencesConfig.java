package eu.pierrebeitz.springfastlymultipartbug;

import com.signalsciences.servlet.filter.SigSciFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

@Configuration
@EnableWebMvc
public class SignalSciencesConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SignalSciencesConfig.class);

    @Value("${rasp.enabled}")
    boolean enabled;

    @Bean
    public FilterRegistrationBean<Filter> signalSciencesRegistration() {
        var filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new RaspFilter(enabled));
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setInitParameters(
                Map.of(
                        "rpcServerURI", "unix:/sigsci/tmp/sigsci.sock",
                        "multipartParsingEnabled", "true"
                ));
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    static class RaspFilter implements Filter {
        private final boolean enabled;
        private Filter sigSciFilter;

        RaspFilter(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            if (enabled) {
                LOG.info("Activating Signal Sciences Filter");
                sigSciFilter = new SigSciFilter();
                sigSciFilter.init(filterConfig);
            } else {
                LOG.info("Signal Sciences Filter is not activated!");
            }
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            if (sigSciFilter != null) {
                sigSciFilter.doFilter(servletRequest, servletResponse, filterChain);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }

        @Override
        public void destroy() {
            if (sigSciFilter != null) {
                sigSciFilter.destroy();
                sigSciFilter = null;
            }
        }
    }
}