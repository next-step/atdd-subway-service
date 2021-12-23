package nextstep.subway.path.domain;

public enum AgeDiscountPolicy {
    GENERAL(0, 0, 0, 1.0), 
    CHILD(6, 12, 350, 0.5), 
    YOUTH(13, 18, 350, 0.8);
    
    public final int ageRangeMin;
    public final int ageRangeMax;
    public final int discountFare;
    public final double discountPercent;

    AgeDiscountPolicy(int ageRangeMin, int ageRangeMax, int discountFare, double discountPercent) {
        this.ageRangeMin = ageRangeMin;
        this.ageRangeMax = ageRangeMax;
        this.discountFare = discountFare;
        this.discountPercent = discountPercent;
    }
    
}
