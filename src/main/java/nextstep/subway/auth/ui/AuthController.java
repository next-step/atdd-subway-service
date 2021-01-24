package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("login/token")
    public ResponseEntity<LoginMember> login(@RequestParam String token) {
        LoginMember member = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(member);
    }
}
