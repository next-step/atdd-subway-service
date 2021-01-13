package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.Collection;

public class PaymentCalculator {
    private static final int DEFAULT_PAYMENT = 1_250;
    private static final int DEFAULT_CHARGE = 0;

    public static int calculatePayment(Collection<Line> lines, int distance) {
        return DEFAULT_PAYMENT + calculateLineCharge(lines) + calculateDistanceCharge(distance);
    }

    private static int calculateDistanceCharge(int distance) {
        return DistanceCharge.calculateCharge(distance);
    }

    private static int calculateLineCharge(Collection<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getCharge)
                .max()
                .orElse(DEFAULT_CHARGE);
    }

    private enum DistanceCharge {
        MIDDLE(10, 5),
        LONG(50, 8)
        ;

        private static final int DEFAULT_DISTANCE = 10;
        private static final int CHARGE_PER_INTERVAL = 100;

        private final int threshold;
        private final int interval;

        DistanceCharge(int threshold, int interval) {
            this.threshold = threshold;
            this.interval = interval;
        }

        public static int calculateCharge(int distance) {
            if (LONG.checkDistance(distance)) {
                return LONG.calculate(distance);
            }
            if (MIDDLE.checkDistance(distance)) {
                return MIDDLE.calculate(distance);
            }
            return DEFAULT_CHARGE;
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
