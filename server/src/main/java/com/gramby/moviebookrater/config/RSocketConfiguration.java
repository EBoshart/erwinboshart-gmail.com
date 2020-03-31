package com.gramby.moviebookrater.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
public class RSocketConfiguration {

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        var mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(strategies);
        return mh;
    }

   @Bean
   PayloadSocketAcceptorInterceptor payloadSocketSecurityInterceptor(RSocketSecurity rSocketSecurity) {
       return rSocketSecurity.authorizePayload(authorizePayloadsSpec ->
               authorizePayloadsSpec.route("hello").authenticated().anyExchange().permitAll())
               .simpleAuthentication(Customizer.withDefaults()).build();
   }

   @Bean
   MapReactiveUserDetailsService mapReactiveUserDetailsService() {
       var erwin = User.withDefaultPasswordEncoder().username("erwin").password("pw").roles("admin").build();
       var assistant = User.withDefaultPasswordEncoder().username("assistant").password("pw").roles("user").build();
       return new MapReactiveUserDetailsService(erwin, assistant);
   }



}
