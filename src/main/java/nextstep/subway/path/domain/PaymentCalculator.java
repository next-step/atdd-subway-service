package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class PaymentCalculator {
    private static final int DEFAULT_PAYMENT = 1_250;
    private static final int DEFAULT_CHARGE = 0;

    public static int calculatePayment(List<Line> lines, int distance) {
        return DEFAULT_PAYMENT + calculateLineCharge(lines) + calculateDistanceCharge(distance);
    }

    private static int calculateDistanceCharge(int distance) {
        return DistanceCharge.calculateCharge(distance);
    }

    private static int calculateLineCharge(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getCharge)
                .max()
                .orElse(DEFAULT_CHARGE);
    }

    private enum DistanceCharge {
        SHORT {
            @Override
            protected int calculate(int distance) {
                return DEFAULT_CHARGE;
            }
        },
        MIDDLE(10, 5),
        LONG(50, 8)
        ;

        private static final int DEFAULT_DISTANCE = 10;
        private static final int CHARGE_PER_INTERVAL = 100;

        private int threshold;
        private int interval;

        DistanceCharge(){}

        DistanceCharge(int threshold, int interval) {
            this.threshold = threshold;
            this.interval = interval;
        }

        public static int calculateCharge(int distance) {
            DistanceCharge distanceCharge = SHORT;
            if (LONG.checkDistance(distance)) {
                distanceCharge = LONG;
            }
            if (MIDDLE.checkDistance(distance)) {
                distanceCharge = MIDDLE;
            }
            return distanceCharge.calculate(distance);
        }

        private boolean checkDistance(int distance) {
            return threshold < distance;
        }

        protected int calculate(int distance) {
            int overDistance = distance - DEFAULT_DISTANCE;
            return ((overDistance - 1) / interval + 1) * CHARGE_PER_INTERVAL;
        }
    }
}
