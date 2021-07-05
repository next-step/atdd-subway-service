package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.fare.discount.ChildDiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.discount.DiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.discount.TeenagerDiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.distance.GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.distance.GraterThan50KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.distance.NotMoreThan10KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.distance.OverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.line.DefaultOverFareByLineStrategy;
import nextstep.subway.path.domain.policy.fare.line.OverFareByLineStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyConfig {
    private static Map<Class, List<Object>> configMap = new HashMap<>();

    static {
        List<Object> discountByAgeStrategies = new ArrayList<>();
        discountByAgeStrategies.add(childDiscountByAgeStrategy());
        discountByAgeStrategies.add(teenagerDiscountByAgeStrategy());

        List<Object> overFareByDistanceStrategies = new ArrayList<>();
        overFareByDistanceStrategies.add(notMoreThan10KmOverFareByDistanceStrategy());
        overFareByDistanceStrategies.add(graterThan10KmNotMoreThan50KmOverFareByDistanceStrategy());
        overFareByDistanceStrategies.add(graterThan50KmOverFareByDistanceStrategy());

        List<Object> overFareByLineStrategies = new ArrayList<>();
        overFareByLineStrategies.add(defaultOverFareByLineStrategy());

        configMap.put(DiscountByAgeStrategy.class, discountByAgeStrategies);
        configMap.put(OverFareByDistanceStrategy.class, overFareByDistanceStrategies);
        configMap.put(OverFareByLineStrategy.class, overFareByLineStrategies);
    }

    public static List<Object> get(Class key) {
        return configMap.get(key);
    }

    public static ChildDiscountByAgeStrategy childDiscountByAgeStrategy() {
        return new ChildDiscountByAgeStrategy();
    }

    public static TeenagerDiscountByAgeStrategy teenagerDiscountByAgeStrategy() {
        return new TeenagerDiscountByAgeStrategy();
    }

    public static NotMoreThan10KmOverFareByDistanceStrategy notMoreThan10KmOverFareByDistanceStrategy() {
        return new NotMoreThan10KmOverFareByDistanceStrategy();
    }

    public static GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy graterThan10KmNotMoreThan50KmOverFareByDistanceStrategy() {
        return new GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy();
    }

    public static GraterThan50KmOverFareByDistanceStrategy graterThan50KmOverFareByDistanceStrategy() {
        return new GraterThan50KmOverFareByDistanceStrategy();
    }

    public static DefaultOverFareByLineStrategy defaultOverFareByLineStrategy() {
        return new DefaultOverFareByLineStrategy();
    }
}
