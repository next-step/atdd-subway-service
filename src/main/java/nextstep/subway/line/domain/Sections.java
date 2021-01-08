package nextstep.subway.line.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.application.AddLineException;
import nextstep.subway.line.application.RemoveLineException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        Stations stations = getStations();
        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        checkLineStationAddable(stations, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted || isDownStationExisted) {
            updateSection(section.getUpStation(), section.getDownStation(), section.getDistance());
        }

        sections.add(section);
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return Stations.emptyStations();
        }

        Stations stations = new Stations();
        Station downStation = findUpStation();

        while (downStation != null) {
            stations.add(downStation);
            Station finalDownStation = downStation;
            downStation = findSection(section -> section.matchUpStation(finalDownStation))
                    .map(Section::getDownStation)
                    .orElse(null);
        }

        return stations;
    }

    public void removeSection(Station station) {
        checkSectionSizeOneOrZero();

        Optional<Section> upLineStation = findSection(section -> section.matchUpStation(station));
        Optional<Section> downLineStation = findSection(section -> section.matchDownStation(station));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(Section.combine(upLineStation.get(), downLineStation.get()));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void updateSection(Station upStation, Station downStation, int distance) {
        for (Section section : sections) {
            UpdateSectionType.valueOf(section, upStation, downStation)
                    .updateSection(section, upStation, downStation, distance);
        }
    }

    private void checkLineStationAddable(Stations stations,
                                         boolean isUpStationExisted,
                                         boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new AddLineException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new AddLineException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkSectionSizeOneOrZero() {
        if (sections.size() <= 1) {
            throw new RemoveLineException("Cannot delete a single section");
        }
    }

    private Station findUpStation() {
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return findSection(section -> !downStations.contains(section.getUpStation()))
                .map(Section::getUpStation)
                .orElseThrow(IllegalStateException::new);
    }

    private Optional<Section> findSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }
}
