package nextstep.subway.member.constant;

public enum MemberDiscountPolicy {
    CHILD(0.5), TEENAGER(0.2), GENERAL(0);


    private final double discountPercent;

    MemberDiscountPolicy(double discountPercent){
        this.discountPercent = discountPercent;
    }

    public static MemberDiscountPolicy convert(Integer age) {
        if(isTeenager(age)){
            return TEENAGER;
        }
        if(isChild(age)){
            return CHILD;
        }
        return GENERAL;
    }

    private static boolean isTeenager(Integer age) {
        return age >= 13 && age < 19;
    }

    private static boolean isChild(Integer age) {
        return age >= 6 && age < 13;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
