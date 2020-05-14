package com.gramby.pictionary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
//import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

/**
 * @author Rob Winch
 */
@EnableReactiveMethodSecurity
public class RSocketSecurityConfig {


    @Bean
    PayloadSocketAcceptorInterceptor rsocketSecurity(RSocketSecurity rsocket) {
        rsocket
                .authorizePayload(authz ->
                        authz
                                .anyExchange().permitAll()
                );

        return rsocket.build();
    }
}
