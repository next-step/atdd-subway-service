package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.section.Money;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.math.BigDecimal;
import java.util.Comparator;

public class PareCalculate {

    private static final BigDecimal BASE_PARE_MONEY = BigDecimal.valueOf(1250);
    private static final int BASE_DISTANCE = 10;
    private static final BigDecimal BASE_MEMBER_DISCOUNT = BigDecimal.valueOf(350);
    private static final BigDecimal YOUTH_DISCOUNT = BigDecimal.valueOf(0.8);
    private static final BigDecimal CHILD_DISCOUNT = BigDecimal.valueOf(0.5);
    private static final int NOT_OVER_DISTANCE = 11;
    private static final int DISTANCE_FIFTY = 50;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_PARE = 100;
    private static final int DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE = 5;
    private static final int DISTANCE_FIFTY_OVER_PLUS_PARE = 100;
    private static final int DISTANCE_FIFTY_OVER_DIVIDE = 8;


    public static Money getPareMoney(LoginMember loginMember, GraphPath<Station, SectionEdge> path, int distance) {
        Money resultPareMoney = Money.from(BASE_PARE_MONEY);
        resultPareMoney.addMoney(getMaxPlusPare(path));
        resultPareMoney.addMoney(getPareMoneyDistanceCalculate(distance));

        return getPareMoneyMemberCalculate(loginMember, resultPareMoney);
    }

    private static Money getPareMoneyMemberCalculate(LoginMember loginMember, Money resultPareMoney) {
        if (!loginMember.isGuest()) {
            getMemberDiscount(loginMember, resultPareMoney);
        }
        return resultPareMoney;
    }

    private static Money getMemberDiscount(LoginMember loginMember, Money resultPareMoney) {
        if (loginMember.isChild()) {
            resultPareMoney.subtractMoney(Money.from(BASE_MEMBER_DISCOUNT));
            resultPareMoney.multiplyMoney(CHILD_DISCOUNT);
        }
        if (loginMember.isYouth()) {
            resultPareMoney.subtractMoney(Money.from(BASE_MEMBER_DISCOUNT));
            resultPareMoney.multiplyMoney(YOUTH_DISCOUNT);
        }
        return resultPareMoney;
    }

    private static Money getPareMoneyDistanceCalculate(int distance) {
        if (distance < NOT_OVER_DISTANCE || (distance - BASE_DISTANCE) < NOT_OVER_DISTANCE) {
            return Money.ofZero();
        }
        if (NOT_OVER_DISTANCE < distance && distance <= DISTANCE_FIFTY) {
            return Money.from(
                    BigDecimal.valueOf(Math.floor(distance / DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_DIVIDE) * DISTANCE_BETWEEN_ELEVEN_AND_FIFTY_PLUS_PARE)
            );
        }
        return Money.from(BigDecimal.valueOf(Math.floor(distance / DISTANCE_FIFTY_OVER_DIVIDE) * DISTANCE_FIFTY_OVER_PLUS_PARE));
    }

    private static Money getMaxPlusPare(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(it -> it.getLine().getPlusPare())
                .max(Comparator.comparing(it -> it.getMoney()))
                .orElse(Money.ofZero());
    }

}
