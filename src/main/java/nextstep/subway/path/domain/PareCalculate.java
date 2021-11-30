package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PareCalculate {

    private static final int BASE_PARE_MONEY = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int BASE_MEMBER_DISCOUNT = 350;
    private static final double YOUTH_DISCOUNT = 0.8;
    private static final double CHILD_DISCOUNT = 0.5;
    private static final int NOT_OVER_DISTANCE = 11;
    private static final int DISTANCE_FIFTY = 50;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_PARE = 100;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE = 5;
    private static final int DISTANCE_FIFTY_OVER_PLUS_PARE = 100;
    private static final int DISTANCE_FIFTY_OVER_DIVIDE = 8;


    public static BigDecimal getPareMoney(LoginMember loginMember, GraphPath<Station, SectionEdge> path, int distance) {
        BigDecimal resultPareMoney = BigDecimal.valueOf(BASE_PARE_MONEY);
        resultPareMoney = resultPareMoney.add(getMaxPlusPare(path));
        resultPareMoney = resultPareMoney.add(getPareMoneyDistanceCalculate(distance));
        return getPareMoneyMemberCalculate(loginMember, resultPareMoney).setScale(0, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal getPareMoneyMemberCalculate(LoginMember loginMember, BigDecimal resultPareMoney) {
        if (!loginMember.isGuest()) {
            getMemberDiscount(loginMember, resultPareMoney);
        }
        return resultPareMoney;
    }

    private static BigDecimal getMemberDiscount(LoginMember loginMember, BigDecimal resultPareMoney) {
        if (loginMember.isChild()) {
            resultPareMoney.subtract(BigDecimal.valueOf(BASE_MEMBER_DISCOUNT));
            resultPareMoney.multiply(BigDecimal.valueOf(CHILD_DISCOUNT));
        }
        if (loginMember.isYouth()) {
            resultPareMoney.subtract(BigDecimal.valueOf(BASE_MEMBER_DISCOUNT));
            resultPareMoney.multiply(BigDecimal.valueOf(YOUTH_DISCOUNT));
        }
        return resultPareMoney;
    }

    private static BigDecimal getPareMoneyDistanceCalculate(int distance) {
        if (distance <= BASE_DISTANCE) {
            return BigDecimal.ZERO;
        }
        if (NOT_OVER_DISTANCE < distance && distance <= DISTANCE_FIFTY) {
            return BigDecimal.valueOf(calculateOverFare((distance - BASE_DISTANCE), DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE, DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_PARE));

        }
        return BigDecimal.valueOf(calculateOverFare((distance - BASE_DISTANCE), DISTANCE_FIFTY_OVER_DIVIDE, DISTANCE_FIFTY_OVER_PLUS_PARE));
    }

    private static int calculateOverFare(int distance, int divide, int plusPare) {
        return (int) ((Math.ceil((distance - 1) / divide) + 1) * plusPare);
    }

    private static BigDecimal getMaxPlusPare(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(it -> it.getLine().getPlusPare())
                .max((o1, o2) -> o1.compareTo(o2))
                .orElse(BigDecimal.ZERO);
    }

}
