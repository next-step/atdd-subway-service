package nextstep.subway.line.domain;

import static java.lang.Integer.*;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class AdditionalCost implements Comparable<AdditionalCost>{
    private int cost;

    public AdditionalCost() {}

    public AdditionalCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdditionalCost)) return false;
        AdditionalCost that = (AdditionalCost) o;
        return cost == that.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost);
    }

    @Override
    public int compareTo(AdditionalCost target) {
        return compare(cost, target.getCost());
    }
}
