package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Sections;

public final class FarePolicy {

    private static final Fare DEFAULT_FARE = Fare.from(1250);

    private final LoginMember member;
    private final Sections sections;

    private FarePolicy(LoginMember member, Sections sections) {
        Assert.notNull(member, "로그인 유저는 필수입니다.");
        Assert.notNull(sections, "구간들은 필수입니다.");
        this.member = member;
        this.sections = sections;
    }

    public static FarePolicy of(LoginMember member, Sections sections) {
        return new FarePolicy(member, sections);
    }

    public Fare fare() {
        return DEFAULT_FARE;
    }
}
