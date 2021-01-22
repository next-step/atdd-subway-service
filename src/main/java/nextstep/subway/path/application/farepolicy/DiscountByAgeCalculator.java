package nextstep.subway.path.application.farepolicy;

import nextstep.subway.auth.domain.LoginMember;

public class DiscountByAgeCalculator {

    public static final int SALE_RATE_FOR_TEENAGER = 20;            // 청소년 할인율
    public static final int SALE_RATE_FOR_CHILDREN = 50;            // 어린이 할인율
    public static final int ADULT_BOUND_AGE = 19;                   // 성인 나이 기준
    public static final int TEENAGER_BOUND_AGE = 13;                // 청소년 나이 기준
    public static final int CHILDREN_BOUND_AGE = 6;                 // 어린이 나이 기준

    /**
     * 사용자의 나이를 반환합니다.
     * 단, 사용자의 나이가 비어오는 경우(비로그인시와 같이) 기본 운임을 계산하도록 0을 반환합니다.
     * @param loginMember
     * @return
     */
    private static int getAge(LoginMember loginMember) {
        if(loginMember.getAge() != null) {
            return loginMember.getAge();
        }
        return 0;
    }

    /**
     * 사용자가 할인 받을 수 있는 나이면 주어진 운임에 할인 값을 적용 합니다.
     * @param fare
     * @param age
     * @return
     */
    public static int saleForAge(int fare, int age) {
        if(DiscountByAgeCalculator.isTeenager(age)) {
            return DiscountByAgeCalculator.calculateSaleFare(fare, DiscountByAgeCalculator.SALE_RATE_FOR_TEENAGER);
        }

        if(DiscountByAgeCalculator.isChildren(age)) {
            return DiscountByAgeCalculator.calculateSaleFare(fare, DiscountByAgeCalculator.SALE_RATE_FOR_CHILDREN);
        }

        return fare;
    }

    /**
     * 사용자가 어린이인지 확인 합니다.
     * @param age
     * @return
     */
    private static boolean isChildren(int age) {
        return age >= DiscountByAgeCalculator.CHILDREN_BOUND_AGE && age < DiscountByAgeCalculator.TEENAGER_BOUND_AGE;
    }

    /**
     * 사용자가 청소년인지 확인 합니다.
     * @param age
     * @return
     */
    private static boolean isTeenager(int age) {
        return age >= DiscountByAgeCalculator.TEENAGER_BOUND_AGE && age < DiscountByAgeCalculator.ADULT_BOUND_AGE;
    }

    /**
     * 비율에 따라 할인 값을 적용하여 계산합니다.
     * @param fare
     * @param saleRate
     * @return
     */
    private static int calculateSaleFare(int fare, int saleRate) {
        return (int) (((100 - saleRate) * 0.01) * fare);
    }


}
