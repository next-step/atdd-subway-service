package nextstep.subway.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok().body(token);
    }
}
