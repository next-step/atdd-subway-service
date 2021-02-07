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

    public Sections(Line line, Station upStation, Station downStation, int distance) {
        Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        boolean isUpStationExisted = isStationExisted(stations, upStation);
        boolean isDownStationExisted = isStationExisted(stations, downStation);

        validate(stations, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            upSection(upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            downSection(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        }
    }

    private void validate(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
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

    private Optional<Section> upSection(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> downSection(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();
    }

    private boolean isStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public void removeSection(Line line, Station station) {
        if (line.getStations().size() <= 2) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = upSection(station);
        Optional<Section> downLineStation = downSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }
}
