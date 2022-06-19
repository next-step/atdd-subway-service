package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MIN_SECTION_NUMBER = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if(ObjectUtils.isNotEmpty(sections)) {
            Stations stations = getStationsInOrder();
            relocate(section, stations);
        }
        sections.add(section);
    }

    private void relocate(Section section, Stations stations) {
        boolean isUpStationExisted = stations.isAnyMatchUpStation(section);
        boolean isDownStationExisted = stations.isAnyMatchDownStation(section);

        validateDuplicateSection(isUpStationExisted, isDownStationExisted);
        validateNoneMatchStation(section, stations);

        if(isUpStationExisted) {
            mergeUpStation(section);
        }
        if(isDownStationExisted) {
            mergeDownStation(section);
        }
    }

    private void mergeUpStation(Section addSection) {
        findMergeTargetUpStation(addSection)
                .ifPresent(section -> section.updateUpStation(addSection.getDownStation(), addSection.getDistance()));
    }

    private void mergeDownStation(Section addSection) {
        findMergeTargetDownStation(addSection)
                .ifPresent(section -> section.updateDownStation(addSection.getUpStation(), addSection.getDistance()));
    }

    private Optional<Section> findMergeTargetUpStation(Section addSection) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(addSection.getUpStation()))
                .findFirst();
    }

    private Optional<Section> findMergeTargetDownStation(Section addSection) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(addSection.getUpStation()))
                .findFirst();
    }

    private void validateDuplicateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNoneMatchStation(Section section, Stations stations) {
        if (stations.isNoneMatchStation(section)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return getStationsInOrder().getStations();
    }

    private Stations getStationsInOrder() {
        Stations stations = new Stations();
        Station station = findFirstUpStation();

        Map<Station, Station> map = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        while (map.get(station) != null) {
            stations.add(station);
            station = map.get(station);
        }
        stations.add(station);
        return stations;
    }

    private Station findFirstUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(this::isStartStation)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isStartStation(Station upStation) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation().equals(upStation));
    }

    public void deleteStation(Station station, Line line) {
        if (sections.size() <= MIN_SECTION_NUMBER) {
            throw new IllegalArgumentException("마지막 구간은 삭제할 수 없음");
        }

        Optional<Section> sectionContainUpStation = findSectionContainUpStation(station);
        Optional<Section> sectionContainDownStation = findSectionContainDownStation(station);

        if(!sectionContainUpStation.isPresent() && !sectionContainDownStation.isPresent()) {
            throw new IllegalArgumentException("노선에 등록되어있지 않은 역입니다.");
        }

        if (sectionContainUpStation.isPresent() && sectionContainDownStation.isPresent()) {
            sections.add(Section.merge(line, sectionContainDownStation.get(), sectionContainUpStation.get()));
        }

        sectionContainUpStation.ifPresent(sections::remove);
        sectionContainDownStation.ifPresent(sections::remove);
    }

    private Optional<Section> findSectionContainDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> findSectionContainUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
    }

}
