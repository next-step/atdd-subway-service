package nextstep.subway.favorite.dto;

import nextstep.subway.auth.domain.LoginMember;

public class FavoriteRequest {
    private LoginMember user;
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(LoginMember user, Long source, Long target) {
        this.user = user;
        this.source = source;
        this.target = target;
    }

    public LoginMember getUser() {
        return user;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
