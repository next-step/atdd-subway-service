package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private List<Station> values = new ArrayList<>();

    public Stations(List<Station> stations) {
        values.addAll(stations);
    }

    public void validateDuplication(Station upStation, Station downStation) {
        if (isExisted(upStation) && isExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public boolean isExisted(Station station) {
        return values.stream().anyMatch(v -> v == station);
    }

    public void validateStation(Station upStation, Station downStation) {
        if (!values.isEmpty() && isAllMatch(upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    private boolean isAllMatch(Station upStation, Station downStation) {
        return isMatch(upStation) && isMatch(downStation);
    }

    private boolean isMatch(Station station) {
        return values.stream().noneMatch(v -> v == station);
    }
}
