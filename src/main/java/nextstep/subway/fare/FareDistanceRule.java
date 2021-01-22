package nextstep.subway.fare;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public enum FareDistanceRule {
    추가운임_50km_초과(8, 100, 50),
    추가운임_10km_초과_50km_이내(5, 100, 10);

    private static final long DEFAULT_FARE = 1250;

    private final int fareDistance;
    private final long surcharge;
    private final int minDistance;

    FareDistanceRule(int fareDistance, long surcharge, int minDistance) {
        this.fareDistance = fareDistance;
        this.surcharge = surcharge;
        this.minDistance = minDistance;
    }

    public int getFareDistance() {
        return fareDistance;
    }

    public long getSurcharge() {
        return surcharge;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public static long findFareByDistance(int distance) {
        List<FareDistanceRule> rules = Arrays.stream(FareDistanceRule.values())
                .sorted(Comparator.comparing(FareDistanceRule::getMinDistance).reversed())
                .collect(Collectors.toList());

        long fare = DEFAULT_FARE;

        for (FareDistanceRule rule : rules) {
            if (isRuleDistance(distance, rule)) {
                fare += findSurcharge(rule, distance - rule.getMinDistance());
                distance = rule.getMinDistance();
            }
        }

        return fare;
    }

    private static boolean isRuleDistance(int distance, FareDistanceRule rule) {
        return distance > rule.getMinDistance();
    }

    private static long findSurcharge(FareDistanceRule fareDistanceRule, int distance) {
        int share = distance / fareDistanceRule.getFareDistance();
        if (!isDivided(fareDistanceRule, distance)) share++;
        return share * fareDistanceRule.getSurcharge();
    }

    private static boolean isDivided(FareDistanceRule fareDistanceRule, int distance) {
        return distance % fareDistanceRule.getFareDistance() == 0;
    }
}
