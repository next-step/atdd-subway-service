package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public List<Station> getStations() {
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = values.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = values.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = values.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    public List<Section> values() {
        return values;
    }

    public void addSection(Section section) {
        checkSection(section);

        values.stream()
            .filter(s -> s.matchesOnlyOneEndOf(section))
            .findAny()
            .ifPresent(s -> s.updateSection(section));

        values.add(section);
    }

    private void checkSection(Section section) {
        if (Objects.isNull(section)) {
            throw new IllegalArgumentException("null 인 구간을 추가할 수는 없습니다.");
        }

        if (!values.isEmpty() && allOrNothingMatches(section)) {
            throw new InvalidSectionException("한쪽 역만 노선에 존재해야 합니다.");
        }
    }

    private boolean allOrNothingMatches(Section section) { // XOR Existence Check
        List<Station> stations = getStations();
        return stations.contains(section.getUpStation())
                == stations.contains(section.getDownStation());
    }

    public void removeStation(Station station) {
        // TODO removeStation
    }
}
