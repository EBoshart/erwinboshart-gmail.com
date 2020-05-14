package com.gramby.pictionary.config;

import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketConfiguration {
    //
    private static final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
    private static final UsernamePasswordMetadata userNamePasswordMetaData = new UsernamePasswordMetadata("erwin", "pw");
//
    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder.setupMetadata(userNamePasswordMetaData, mimeType).connectTcp("localhost", 8888).block();
    }
//

    @Bean
    RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }
}
