package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DuplicateSectionException;
import nextstep.subway.line.exception.MinimumSectionException;
import nextstep.subway.line.exception.NotFoundSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    private Station findFirstStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSectionWithDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }
    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = new Station();
        if (sections.size() > 0) {
            downStation = findFirstStation();
            stations.add(downStation);
        }

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSectionWithUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    Optional<Section> findSectionWithUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    Optional<Section> findSectionWithDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
    }

    private boolean isExistStation(Station station) {
        return getStations().stream().anyMatch(it -> it.equals(station));
    }

    public void addSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();
        List<Station> stations = getStations();

        if (isExistStation(upStation) && isExistStation(downStation)) {
            throw new DuplicateSectionException();
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (!isExistStation(upStation) && !isExistStation(downStation) ) {
            throw new NotFoundSectionException();
        }

        if (isExistStation(upStation)) {
            findSectionWithUpStation(upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
            sections.add(section);
            return;
        }

        if (isExistStation(downStation)) {
            findSectionWithDownStation(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
            sections.add(section);
            return;
        }
        sections.add(section);
    }

    public void removeStation(Station station, Line line) {
        if (sections.size() <= 1) {
            throw new MinimumSectionException();
        }

        Optional<Section> upLineStation = findSectionWithUpStation(station);
        Optional<Section> downLineStation = findSectionWithDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }


}
