package nextstep.subway.line.domain;


import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Line line, Station upStation, Station downStation, int distance) {
        validate(upStation, downStation);
        updateUpStationIfPresent(line, upStation, downStation, distance);
        updateDownStationIfPresent(line, upStation, downStation, distance);
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void updateUpStationIfPresent(Line line, Station upStation, Station downStation, int distance) {
        if (!isPresentStation(upStation)) {
            return;
        }

        sections.stream()
                .filter(it -> it.equalsUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStationIfPresent(Line line, Station upStation, Station downStation, int distance) {
        if (!isPresentStation(downStation)) {
            return;
        }

        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void validate(Station upStation, Station downStation) {
        if (isPresentStation(upStation) && isPresentStation(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations().isEmpty() && !isPresentStation(upStation) && !isPresentStation(downStation) ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isPresentStation(Station station) {
        return stations().stream().anyMatch(it -> it == station);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = firstStation();
        stations.add(station);

        while (station != null) {
            Station finalDownStation = station;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            station = nextLineStation.get().getDownStation();
            stations.add(station);
        }

        return stations;
    }

    private Station firstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void remove(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }
}
