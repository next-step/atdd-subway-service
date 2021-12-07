package nextstep.subway.line.domain;

import nextstep.subway.line.exception.section.SectionDuplicatedException;
import nextstep.subway.line.exception.section.SectionNoStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotDeleteException;
import nextstep.subway.station.exception.StationNotFoundException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Sections
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@Embeddable
public class Sections {
    @Transient
    public static final int MIN_SECTION_COUNT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        addValidate(section.getUpStation(), section.getDownStation());

        if (!getStations().isEmpty()) {
            sections.stream()
                    .filter(it -> it.upStationEqualTo(section.getUpStation()) || it.downStationEqualTo(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateSection(section));
        }

        sections.add(section);
    }

    private void addValidate(Station upStation, Station downStation) {
        if (isExistStation(upStation) && isExistStation(downStation)) {
            throw new SectionDuplicatedException();
        }

        if (isInvalidStations(upStation, downStation)) {
            throw new SectionNoStationException();
        }
    }

    private boolean isExistStation(Station station) {
        return getStations().stream()
                .anyMatch(it -> it.equals(station));
    }

    private boolean isInvalidStations(Station upStation, Station downStation) {
        return !getStations().isEmpty() && !isExistStation(upStation) && !isExistStation(downStation);
    }

    public void remove(Long stationId) {
        removeValidate(stationId);

        if (lastStation().equals(stationId)) {
            sections.remove(lastSection());
            return;
        }

        Section section = getSection(stationId);
        findPrevSection(section.getUpStation()).ifPresent(
                prev -> prev.removeSection(section)
        );
        sections.remove(section);
    }

    private void removeValidate(Long stationId) {
        if (sections.size() <= MIN_SECTION_COUNT) {
            throw new StationNotDeleteException();
        }

        if (isNotExistStation(stationId)) {
            throw new StationNotFoundException();
        }
    }

    private boolean isNotExistStation(Long stationId) {
        return getStations()
                .stream()
                .noneMatch(station -> station.equals(stationId));
    }

    public List<Section> getList() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return addOrderedStations(new ArrayList<>(), firstStation());
    }

    private List<Station> addOrderedStations(List<Station> stations, Station station) {
        stations.add(station);
        if (!station.equals(lastStation())) {
            return addOrderedStations(stations, getNextStation(station));
        }
        return stations;
    }

    private Station firstStation() {
        return sections.stream()
                .filter(section -> !getDownStations().contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new)
                .getUpStation();
    }

    private Station lastStation() {
        return sections.stream()
                .filter(section -> !getUpStations().contains(section.getDownStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new)
                .getDownStation();
    }

    private Section lastSection() {
        return sections.stream()
                .filter(section -> section.downStationEqualTo(lastStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private Station getNextStation(Station station) {
        return sections.stream().filter(section -> section.upStationEqualTo(station))
                .findFirst()
                .orElseThrow(StationNotFoundException::new)
                .getDownStation();
    }

    private Section getSection(Long stationId) {
        return sections.stream()
                .filter(section -> section.upStationEqualTo(stationId))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private Optional<Section> findPrevSection(Station station) {
        return sections.stream()
                .filter(section -> section.downStationEqualTo(station))
                .findFirst();
    }
}
