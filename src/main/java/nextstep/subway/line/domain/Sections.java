package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    protected Sections() {
    }

    public static Sections empty() {
        return new Sections();
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public boolean addSection(Section section) {
        return this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
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
                .filter(it -> it.hasUpStation(finalDownStation))
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
            Optional<Section> nextLineStation = findDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeStation(Station station) {
        validateRemoveStation();

        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section section = Section.combine(downLineStation.get(), upLineStation.get());
            sections.add(section);
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findDownLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.hasDownStation(station))
            .findFirst();
    }

    private void validateRemoveStation() {
        if (sections.size() <= 1) {
            throw new SubwayException(SubwayErrorCode.CANNOT_DELETE_LAST_LINE);
        }
    }

    private Optional<Section> findUpLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.hasUpStation(station))
            .findFirst();
    }

    public void addStation(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new SubwayException(SubwayErrorCode.ALREADY_REGISTERED_SECTION);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
            stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new SubwayException(SubwayErrorCode.INVALID_LINE_SECTION);
        }

        if (stations.isEmpty()) {
            addSection(section);
            return;
        }

        if (isUpStationExisted) {
            getSections().stream()
                .filter(it -> it.hasUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            addSection(section);
        } else if (isDownStationExisted) {
            getSections().stream()
                .filter(it -> it.hasDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            addSection(section);
        } else {
            throw new RuntimeException();
        }
    }
}
