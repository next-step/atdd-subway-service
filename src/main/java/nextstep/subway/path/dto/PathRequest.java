package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;

public class PathRequest {
    private Long source;
    private Long target;
    private LoginMember user;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public PathRequest(Long source, Long target, LoginMember user) {
        this.source = source;
        this.target = target;
        this.user = user;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public LoginMember getUser() {
        return user;
    }
}
