package nextstep.subway.line.domain;

import nextstep.subway.line.exception.SectionAddException;
import nextstep.subway.line.exception.SectionSizeMinimunException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
            return Arrays.asList();
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

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = isUpStationExisted(upStation);
        boolean isDownStationExisted = isDownStationExisted(downStation);

        if (getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionAddException(SectionAddException.SECTION_HAS_UP_AND_DOWN_STATION_MSG);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SectionAddException(SectionAddException.SECTION_HAS_NOT_UP_AND_DOWN_STATION_MSG);
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
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
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream().anyMatch(it -> it == downStation);
    }
}
