package odiro.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;

    @ManyToOne
    private Member sender;
    private String content;
    @CreatedDate
    private LocalDateTime timestamp;

    // 생성자, Getter 및 Setter
}