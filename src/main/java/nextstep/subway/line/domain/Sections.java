package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public Optional<Section> getDownStationMathSection(final Station downStation) {
        return sections.stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst();
    }

    public Optional<Section> getUpStationMatchSection(final Station upStation) {
        return sections.stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst();
    }

    public void removeStation(final Line line, final Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = getUpStationMatchSection(station);
        Optional<Section> downLineStation = getDownStationMathSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().add(downLineStation.get().getDistance());
            sections.add(Section.of(line, newUpStation, newDownStation, newDistance.getDistance()));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = getDownStationMathSection(downStation
            );
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = getUpStationMatchSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public boolean isStationNotExists(final Station station) {
        return getStations().stream().noneMatch(it -> it == station);
    }

    public boolean isStationExists(final Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = isStationExists(section.getUpStation());
        boolean isDownStationExisted = isStationExists(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && isStationNotExists(section.getUpStation()) &&
            isStationNotExists(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            getUpStationMatchSection(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
        } else if (isDownStationExisted) {
            getDownStationMathSection(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
        } else {
            throw new RuntimeException();
        }
    }
}
