package nextstep.subway.line.domain;

import nextstep.subway.line.exception.SectionAddException;
import nextstep.subway.line.exception.SectionSizeMinimunException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (Objects.nonNull(downStation)) {
            downStation = nextSectionDownStation(stations, downStation);
        }

        return stations;
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        return nextSectionUpStation(downStation);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = isUpStationExisted(stations, upStation);
        boolean isDownStationExisted = isDownStationExisted(stations, downStation);

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        validAddSection(isUpStationExisted, isDownStationExisted);

        if(isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }

        if(isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(new Section(line, upStation, downStation, distance));
    }

    public void removeSection(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new SectionSizeMinimunException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addNewSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void addNewSection (Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private Station nextSectionDownStation(List<Station> stations, Station downStation) {
        Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getUpStation().equals(downStation))
                .findFirst();

        if (!nextLineStation.isPresent()) {
            return null;
        }

        Station nextStation = nextLineStation.get().getDownStation();
        stations.add(nextStation);
        return nextStation;
    }

    private Station nextSectionUpStation(Station downStation) {
        Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();

        if (!nextLineStation.isPresent()) {
            return downStation;
        }

        return nextSectionUpStation(nextLineStation.get().getUpStation());
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void validAddSection (boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionAddException(SectionAddException.SECTION_HAS_UP_AND_DOWN_STATION_MSG);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SectionAddException(SectionAddException.SECTION_HAS_NOT_UP_AND_DOWN_STATION_MSG);
        }
    }

    private boolean isUpStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private boolean isDownStationExisted(List<Station> stations, Station downStation) {
        return stations.stream().anyMatch(it -> it == downStation);
    }
}
