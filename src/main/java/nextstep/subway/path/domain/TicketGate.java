package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.DistanceFareKind;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.member.domain.MemberKind;

public class TicketGate {

    private TicketGate() {
    }

    public static Fare calculateFare(Fare bigSectionFare, Distance distance, LoginMember loginMember) {
        Fare totalFare = sumDistanceFare(distance).sum(bigSectionFare);
        return discountMemberKindFare(totalFare, loginMember);
    }

    private static Fare sumDistanceFare(Distance distance) {
        DistanceFareKind distanceFareKind = DistanceFareKind.of(distance.distance());
        return distanceFareKind.sumDistanceFare(distance.distance());
    }

    private static Fare discountMemberKindFare(Fare fare, LoginMember loginMember) {
        MemberKind memberKind = MemberKind.of(loginMember.getAge());
        return memberKind.discountFare(fare);
    }
}
