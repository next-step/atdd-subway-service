package nextstep.subway.fee.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int applyStartDistance;
    private int applyEndDistance;
    private int applyDistance;
    private int applyFee;

    public StationFee() {}

    public StationFee(int applyStartDistance, int applyEndDistance, int applyDistance, int applyFee) {
        this.applyStartDistance = applyStartDistance;
        this.applyEndDistance = applyEndDistance;
        this.applyDistance = applyDistance;
        this.applyFee = applyFee;
    }

    public Long getId() {
        return id;
    }

    public int getApplyStartDistance() {
        return applyStartDistance;
    }

    public int getApplyEndDistance() {
        return applyEndDistance;
    }

    public int getApplyDistance() {
        return applyDistance;
    }

    public int getApplyFee() {
        return applyFee;
    }

    public int getAdditionalFee(int distance) {
        return (int) ( Math.ceil((distance - (applyStartDistance - 1)) / applyDistance )) * applyFee;
    }
}
