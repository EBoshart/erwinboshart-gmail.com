package com.gramby.pictionary.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
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
               authorizePayloadsSpec
                       .route("draw.push.coordinate").hasAuthority("ROLE_ADMIN")
                       .anyExchange().authenticated())
               .simpleAuthentication(Customizer.withDefaults()).build();
   }

   @Bean
   MapReactiveUserDetailsService mapReactiveUserDetailsService(PasswordEncoder passwordEncoder) {
        String encryptedPw = passwordEncoder.encode("pw");

       var erwin = User.builder().username("erwin").password(encryptedPw).roles("ADMIN").build();
       var assistant = User.builder().username("assistant").password(encryptedPw).roles("USER").build();
       return new MapReactiveUserDetailsService(erwin, assistant);
   }

   @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }

}
