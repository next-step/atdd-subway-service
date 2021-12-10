package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;

public class SubwayFeeRequest {
    private final int distance;
    private final int lineSurcharge;
    private final LoginMember.AgeType memberAgeType;

    public SubwayFeeRequest(int distance, int lineSurcharge, LoginMember.AgeType memberAgeType) {
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

    public LoginMember.AgeType getMemberAgeType() {
        return memberAgeType;
    }
}
