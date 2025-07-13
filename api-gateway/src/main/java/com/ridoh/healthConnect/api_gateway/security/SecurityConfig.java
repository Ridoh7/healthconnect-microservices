//package com.ridoh.healthConnect.api_gateway.security;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public FilterRegistrationBean<GatewayAuthFilter> gatewayAuthFilter(
//            GatewayAuthFilter filter) {
//
//        FilterRegistrationBean<GatewayAuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(filter);
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//}
//
