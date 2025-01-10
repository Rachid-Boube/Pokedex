import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerSupport;

@Configuration
public class MyCorsConfig extends WebMvcConfigurerSupport {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路径
                .allowedOrigins("*") // 允许的前端域名，可以添加多个
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(false) // 是否允许发送Cookie
                .maxAge(3600); // 预检请求的缓存时间，单位秒
                
    }
}