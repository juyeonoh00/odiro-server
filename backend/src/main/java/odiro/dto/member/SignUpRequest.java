package odiro;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private Long id;
    private String user_id;
    private String password;
    private String name;
    private String email;
}
