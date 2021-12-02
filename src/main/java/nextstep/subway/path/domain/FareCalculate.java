package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FareCalculate {

    private static final int BASE_FARE_MONEY = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int NOT_OVER_DISTANCE = 11;
    private static final int DISTANCE_FIFTY = 50;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_FARE = 100;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE = 5;
    private static final int DISTANCE_FIFTY_OVER_PLUS_FARE = 100;
    private static final int DISTANCE_FIFTY_OVER_DIVIDE = 8;


    public static BigDecimal getFareMoney(LoginMember loginMember, GraphPath<Station, SectionEdge> path, int distance) {
        BigDecimal resultFareMoney = BigDecimal.valueOf(BASE_FARE_MONEY);
        resultFareMoney = resultFareMoney.add(getMaxPlusFare(path));
        resultFareMoney = resultFareMoney.add(getFareMoneyDistanceCalculate(distance));
        resultFareMoney = MemberShip.memberShipCalculate(loginMember, resultFareMoney).setScale(0, RoundingMode.HALF_EVEN);
        return resultFareMoney;
    }

    private static BigDecimal getFareMoneyDistanceCalculate(int distance) {
        if (distance <= BASE_DISTANCE) {
            return BigDecimal.ZERO;
        }
        if (NOT_OVER_DISTANCE < distance && distance <= DISTANCE_FIFTY) {
            return BigDecimal.valueOf(calculateOverFare((distance - BASE_DISTANCE), DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE, DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_FARE));

        }
        return BigDecimal.valueOf(calculateOverFare((distance - BASE_DISTANCE), DISTANCE_FIFTY_OVER_DIVIDE, DISTANCE_FIFTY_OVER_PLUS_FARE));
    }

    private static int calculateOverFare(int distance, int divide, int plusFare) {
        return (int) ((Math.ceil((distance - 1) / divide) + 1) * plusFare);
    }

    private static BigDecimal getMaxPlusFare(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(it -> it.getLine().getPlusFare())
                .max((o1, o2) -> o1.compareTo(o2))
                .orElse(BigDecimal.ZERO);
    }

}
