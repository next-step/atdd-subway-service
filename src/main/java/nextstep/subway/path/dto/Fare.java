package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;

import java.util.Objects;

public class Fare {
    private long won;

    public Fare(long won) {
        this.won = won;
    }

    public void add(Line line) {
        won += line.getExtraFare();
    }

    public long getWon() {
        return won;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fare)) {
            return false;
        }
        Fare fare = (Fare) o;
        return getWon() == fare.getWon();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWon());
    }

    @Override
    public String toString() {
        return "Fare{" + "won=" + won + '}';
    }
}
