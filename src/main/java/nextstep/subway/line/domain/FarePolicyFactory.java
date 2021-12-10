package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.MemberId;

public class FarePolicyFactory {

    public static FarePolicy create(LoginMember loginMember) {
        final MemberId memberId = loginMember.toMemberId();
        if (memberId.hasMember()) {
            return new FareMember(loginMember);
        }
        return new FareGuest();
    }
}
