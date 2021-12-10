package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;

public class SubwayFeeRequest {
    private final int distance;
    private final int lineSurcharge;
    private final boolean guest;
    private final LoginMember.AgeType memberAgeType;

    public SubwayFeeRequest(int distance, int lineSurcharge, boolean guest,
        LoginMember.AgeType memberAgeType) {
        this.distance = distance;
        this.lineSurcharge = lineSurcharge;
        this.guest = guest;
        this.memberAgeType = memberAgeType;
    }

    public int getDistance() {
        return distance;
    }

    public int getLineSurcharge() {
        return lineSurcharge;
    }

    public boolean isGuest() {
        return guest;
    }

    public LoginMember.AgeType getMemberAgeType() {
        return memberAgeType;
    }
}
