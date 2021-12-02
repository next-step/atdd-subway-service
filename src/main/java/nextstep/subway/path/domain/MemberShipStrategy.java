package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

@FunctionalInterface
public interface MemberShipStrategy {
    boolean isMemberShipConfirm(LoginMember member);
}
