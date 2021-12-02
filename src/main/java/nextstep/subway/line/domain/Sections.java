package nextstep.subway.line.domain;

import nextstep.subway.common.exception.section.SectionDuplicateException;
import nextstep.subway.common.exception.section.SectionNotCreateException;
import nextstep.subway.common.exception.station.StationNotDeleteException;
import nextstep.subway.common.exception.station.StationNotFoundException;
import nextstep.subway.station.domain.Station;

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

        if (getStations().isEmpty()) {
            sections.add(section);
            return;
        }

        if (isExistStation(section.getUpStation())) {
            sections.stream()
                    .filter(it -> it.upStationEqualTo(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
            return;
        }

        if (isExistStation(section.getDownStation())) {
            sections.stream()
                    .filter(it -> it.downStationEqualTo(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
        }
    }

    private void addValidate(Station upStation, Station downStation) {
        if (isExistStation(upStation) && isExistStation(downStation)) {
            throw new SectionDuplicateException();
        }

        if (isInvalidStations(upStation, downStation)) {
            throw new SectionNotCreateException();
        }
    }

    private boolean isExistStation(Station station) {
        return getStations().stream()
                .anyMatch(it -> it.equals(station));
    }

    private boolean isInvalidStations(Station upStation, Station downStation) {
        return !getStations().isEmpty() && !isExistStation(upStation) && !isExistStation(downStation);
    }

    public void remove(Line line, Long stationId) {
        removeValidate(stationId);

        Optional<Section> upLineSection = getSectionInUpStation(stationId);
        Optional<Section> downLineSection = getSectionInDownStation(stationId);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            Station newUpStation = downLineSection.get().getUpStation();
            Station newDownStation = upLineSection.get().getDownStation();
            Distance newDistance = Distance.valueOf(DistanceType.PLUS, upLineSection.get().getDistance(), downLineSection.get().getDistance());
            sections.add(Section.of(line, newUpStation, newDownStation, newDistance));

        }
        upLineSection.ifPresent(it -> sections.remove(it));
        downLineSection.ifPresent(it -> sections.remove(it));
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
                .noneMatch(station -> station.getId().equals(stationId));
    }

    private Optional<Section> getSectionInDownStation(Long stationId) {
        return sections.stream()
                .filter(it -> it.getDownStation().getId().equals(stationId))
                .findFirst();
    }

    private Optional<Section> getSectionInUpStation(Long stationId) {
        return sections.stream()
                .filter(it -> it.getUpStation().getId().equals(stationId))
                .findFirst();
    }

    public List<Section> getList() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findFirstStation();

        while (Optional.ofNullable(station).isPresent()) {
            stations.add(station);
            station = findNextStation(station);
        }

        return stations;
    }

    private Station findFirstStation() {
        return sections.stream()
                .filter(section -> !getDownStations().contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(StationNotFoundException::new)
                .getUpStation();
    }

    private List<Station> getDownStations() {
        return sections.stream().map(Section::getDownStation).collect(Collectors.toList());
    }

    private Station findNextStation(Station station) {
        return sections.stream().filter(section -> section.upStationEqualTo(station))
                .findFirst()
                .orElse(new Section())
                .getDownStation();
    }
}
