package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.matchUpStation(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.matchDownStation(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addStation(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::matchUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::matchDownStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(section::matchUpStation) &&
                stations.stream().noneMatch(section::matchDownStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            add(section);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(section::matchUpToUpStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            add(section);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(section::matchDownToDownStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            add(section);
        } else {
            throw new RuntimeException();
        }
    }

    public void remove(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.matchUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.matchDownStation(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }
}
