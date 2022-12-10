package nextstep.subway.path.domain.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.path.SectionEdges;

public class FareStrategy {

    private static final int BASE_FARE = 1250;

    private DistanceFareStrategy distanceFareStrategy;
    private DiscountFareStrategy discountFareStrategy;
    private SectionEdges sectionEdges;


    private FareStrategy(SectionEdges sectionEdges, Distance distance, LoginMember loginMember) {
        this.distanceFareStrategy = new DistanceFareStrategy(distance);
        this.discountFareStrategy = new DiscountFareStrategy(loginMember);
        this.sectionEdges = sectionEdges;
    }

    public static FareStrategy of(SectionEdges sectionEdges, Distance distance, LoginMember loginMember) {
        return new FareStrategy(sectionEdges, distance, loginMember);
    }


    public int getFare() {
        int addedFare = BASE_FARE + distanceFareStrategy.getAdditionalFare()
                + sectionEdges.getMaxSurCharge();
        return discountFareStrategy.getDiscountedFare(addedFare);

    }
}
