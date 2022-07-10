package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
public class LoginCheckService {
    public void needLogin(LoginMember loginMember) {
        if (loginMember.isGuest()) {
            throw new AuthorizationException();
        }
    }
}
