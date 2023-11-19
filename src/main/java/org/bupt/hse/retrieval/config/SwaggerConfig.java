package org.bupt.hse.retrieval.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-03-20
 * swagger地址 http://localhost:8666/swagger-ui/index.html
 * swagger地址 http://10.112.67.227:8666/swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Emoticon Labelling接口文档")
                .description("更多请咨询服务开发者Hu Saier。")
                .contact(new Contact("Hu Saier", "", "husserl@bupt.edu.cn"))
                .version("1.0")
                .build();
    }
}
