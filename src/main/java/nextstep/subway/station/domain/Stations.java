package nextstep.subway.station.domain;

import java.util.Collections;
import java.util.List;

public class Stations {

    private final List<Station> values;

    private Stations(List<Station> values) {
        this.values = values;
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public boolean isContains(Station upStation) {
        return this.values.contains(upStation);
    }

    public List<Station> getValues() {
        return Collections.unmodifiableList(values);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public void validate(Station upStation, Station downStation) {
        boolean upStationContains = this.values.contains(upStation);
        boolean downStationContains = this.values.contains(downStation);

        if (upStationContains && downStationContains) {
            throw new RuntimeException("이미 등록된 구간 입니다.");

        }

        if (!upStationContains && !downStationContains) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }
}
