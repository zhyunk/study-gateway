package kim.zhyun.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    
    public CustomFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String uri = request.getURI().toString();
            
            log.info("💁 %s".formatted(uri));
            
            String method = request.getMethod().name();
            switch (method) {
                case  "GET", "DELETE"   -> log.info("💁 Is the method GET or DELETE --> %s".formatted(method));
                case  "PUT", "PATCH"    -> log.info("💁 update 🔨🔧 --> %s".formatted(method));
                case  "POST"            -> log.info("💁 new save 💾 --> %s".formatted(method));
            }
            
            if (uri.contains("/error") || uri.contains("/exception"))
                return exception(exchange, uri, "my exception");
            
            return chain.filter(exchange);
        };
    }

    
    private static Mono<Void> exception(ServerWebExchange exchange, String uri, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.I_AM_A_TEAPOT);
        
        log.info("💁 exception !! --> %s\n%s".formatted(uri, message));
        return response.setComplete();
    }
    
    public static class Config { }
    
}
