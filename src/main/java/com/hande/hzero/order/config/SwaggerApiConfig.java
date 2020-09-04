package com.hande.hzero.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author huaxin.deng@hand-china.com 2020-08-20 09:25:11
 */
@Configuration
public class SwaggerApiConfig {

    public static final String SO_HEADER = "Hodr SoHeader";
    public static final String SO_LINE = "Hodr SoLine";
    public static final String COMPANY = "Hodr Company";
    public static final String ITEM = "Hodr Item";
    public static final String CUSTOMER = "Hodr Customer";

    @Autowired
    public SwaggerApiConfig(Docket docket){
        docket.tags(
            new Tag(SO_HEADER, "订单头"),
            new Tag(SO_LINE, "订单行"),
            new Tag(COMPANY, "公司"),
            new Tag(CUSTOMER, "客户"),
            new Tag(ITEM, "物料")

        );
    }
}
