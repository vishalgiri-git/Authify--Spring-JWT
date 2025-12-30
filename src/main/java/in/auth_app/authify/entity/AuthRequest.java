package in.auth_app.authify.entity;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
