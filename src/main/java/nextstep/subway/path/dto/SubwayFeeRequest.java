package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;

public class SubwayFeeRequest {
    private final int distance;
    private final int maxLineSurcharge;
    private final boolean guest;
    private final LoginMember.AgeType memberAgeType;

    private SubwayFeeRequest(int distance, int maxLineSurcharge, boolean guest,
        LoginMember.AgeType memberAgeType) {
        this.distance = distance;
        this.maxLineSurcharge = maxLineSurcharge;
        this.guest = guest;
        this.memberAgeType = memberAgeType;
    }

    public static SubwayFeeRequest of(int distance, int maxLineSurcharge, boolean guest,
        LoginMember.AgeType memberAgeType) {
        return new SubwayFeeRequest(distance, maxLineSurcharge, guest, memberAgeType);
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxLineSurcharge() {
        return maxLineSurcharge;
    }

    public boolean isGuest() {
        return guest;
    }

    public LoginMember.AgeType getMemberAgeType() {
        return memberAgeType;
    }
}
