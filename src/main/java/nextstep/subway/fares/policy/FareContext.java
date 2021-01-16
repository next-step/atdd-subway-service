package nextstep.subway.fares.policy;

import lombok.Getter;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;

@Getter
public class FareContext {

    private final Path path;
    private final LoginMember loginMember;

    public FareContext(Path path, LoginMember loginMember) {
        this.path = path;
        this.loginMember = loginMember;
    }
}
