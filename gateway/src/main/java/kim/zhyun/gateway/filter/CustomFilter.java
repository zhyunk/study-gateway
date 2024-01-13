package kim.zhyun.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    
    public CustomFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            String path = req.getPath().toString();
            
            log.info("💁 %s".formatted(path));
            
            String method = req.getMethod().name();
            switch (method) {
                case  "GET", "DELETE"   -> log.info("💁 Is the method GET or DELETE --> %s".formatted(method));
                case  "PUT", "PATCH"    -> log.info("💁 update 🔨🔧 --> %s".formatted(method));
                case  "POST"            -> log.info("💁 new save 💾 --> %s".formatted(method));
            }
            
            if (path.contains("/error") || path.contains("/exception")) {
                req = redirect(exchange, path, "move to /rewrite-path");
            }
            
            return chain.filter(exchange.mutate().request(req).build());
        };
    }

    
    private static ServerHttpRequest redirect(ServerWebExchange exchange, String path, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.I_AM_A_TEAPOT);
        
        log.info("💁 exception !! --> %s\n%s".formatted(path, message));
        
        URI transformedUri = exchange.getRequest().getURI().resolve("/%s/rewrite-path/in".formatted(path.split("/")[1]));

        return exchange.getRequest().mutate()
                .method(GET)
                .uri(transformedUri).build();
    }
    
    public static class Config { }
    
}
