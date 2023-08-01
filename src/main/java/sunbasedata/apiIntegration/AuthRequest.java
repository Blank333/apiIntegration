package sunbasedata.apiIntegration;

import lombok.Data;

@Data
public class AuthRequest {
    private String login_id;
    private String password;
}
