package nextstep.subway.path.dto;

import nextstep.subway.member.domain.MemberAgeType;

public class SubwayFareRequest {
    private final int distance;
    private final int lineSurcharge;
    private final MemberAgeType memberAgeType;

    public SubwayFareRequest(int distance, int lineSurcharge, MemberAgeType memberAgeType) {
        this.distance = distance;
        this.lineSurcharge = lineSurcharge;
        this.memberAgeType = memberAgeType;
    }

    public int getDistance() {
        return distance;
    }

    public int getLineSurcharge() {
        return lineSurcharge;
    }

    public MemberAgeType getMemberAgeType() {
        return memberAgeType;
    }
}
