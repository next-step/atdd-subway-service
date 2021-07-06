package nextstep.subway.path.infra;

import nextstep.subway.path.domain.RatePolicy;

import java.util.Arrays;

/**
 * @see <a href="http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354">운임안내</a>
 */
public class RatePolicyByDistance implements RatePolicy {
    private final double distance;

    public RatePolicyByDistance(final double distance) {
        this.distance = distance;
    }

    @Override
    public double calc(double fee) {
        return calculate() + fee;
    }

    public double calc() {
        return calc(0);
    }

    private double calculate() {
        return RuleByDistance.valueOf(distance).calculate(distance);
    }

    private enum RuleByDistance implements CalculateAble {
        BASIC(0, 11) {
            @Override
            public int calculate(final double distance) {
                return DEFAULT_FEE;
            }
        },
        MEDIUM_DISTANCE(11, 50) {
            @Override
            public int calculate(final double distance) {
                return calculateByDistance(distance - 10, 5) - ADDITIONAL_FEE;
            }
        },
        LONG_DISTANCE(50, 9999) {
            @Override
            public int calculate(final double distance) {
                return calculateByDistance(distance, 8);
            }
        };

        private final int start;
        private final int end;

        private static final int DEFAULT_FEE = 1250;
        private static final int ADDITIONAL_FEE = 100;

        RuleByDistance(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        private static RuleByDistance valueOf(double distance) {
            return Arrays.stream(RuleByDistance.values())
                    .filter(x -> x.start <= distance && x.end > distance)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("지원하지 않는 거리입니다."));
        }

        private static int calculateByDistance(double distance, int km) {
            int addition = (km == 0) ? 0
                    : (int) ((Math.ceil((distance) / km) + 1) * ADDITIONAL_FEE);

            return DEFAULT_FEE + addition;
        }
    }

    private interface CalculateAble {
        int calculate(double distance);
    }

}
