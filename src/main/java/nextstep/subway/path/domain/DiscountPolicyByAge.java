package nextstep.subway.path.domain;

public enum DiscountPolicyByAge {

    CHILDREN_DISCOUNT(6, 12, 350, 0.5),
    YOUTH_DISCOUNT(13, 18, 350, 0.8);

    private int minAge;
    private int maxAge;
    private int deduction;
    private double priceRate;

    DiscountPolicyByAge(int minAge, int maxAge, int deduction, double priceRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deduction = deduction;
        this.priceRate = priceRate;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getPriceRate() {
        return priceRate;
    }
}
