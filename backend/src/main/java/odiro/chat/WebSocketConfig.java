package odiro.chat;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커가 /send로 시작되는 메시지를 클라이언트로 라우팅
        config.enableSimpleBroker("/send");
        //@MessageMapping("/chat")로 메시지를 받을 api의 prefix를 설정
        config.setApplicationDestinationPrefixes("/chat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
//                .withSockJS();
    }
}