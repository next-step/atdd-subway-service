package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int MINIMUM_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true)
    private List<Section> list = new ArrayList<>();

    @Transient
    private Map<Station, Section> upStationToSectionCache;

    @Transient
    private Map<Station, Section> downStationToSectionCache;

    protected Sections() {
    }

    private Sections(Section section) {
        Assert.notNull(section, "초기 구간은 반드시 존재해야 합니다.");
        this.list.add(section);
    }

    public static Sections from(Section section) {
        return new Sections(section);
    }

    List<Station> stations() {
        return upToDownSortedStations();
    }

    void add(Section section) {
        validateAddition(section);
        removeOverlappingSection(section);
        list.add(section);
        deleteSectionCaches();
    }

    void removeStation(Station station) {
        if (isNotExist(station)) {
            return;
        }
        validateSize();
        remove(station);
        deleteSectionCaches();
    }

    private void remove(Station station) {
        Optional<Section> sectionByUpStation = findByUpStation(station);
        Optional<Section> sectionByDownStation = findByDownStation(station);
        if (sectionByUpStation.isPresent() && sectionByDownStation.isPresent()) {
            list.add(sectionByUpStation.get().merge(sectionByDownStation.get()));
        }
        sectionByUpStation.ifPresent(section -> list.remove(section));
        sectionByDownStation.ifPresent(section -> list.remove(section));
    }

    private void validateSize() {
        if (hasMinimumSize()) {
            throw new InvalidDataException("구간은 반드시 한 개 이상 존재해야 합니다.");
        }
    }

    private boolean hasMinimumSize() {
        return list.size() == MINIMUM_SECTION_SIZE;
    }

    private List<Station> upToDownSortedStations() {
        Section firstSection = firstSection();
        List<Station> stations = new ArrayList<>(firstSection.stations());

        Optional<Section> nextSectionOptional = findByUpStation(firstSection.downStation());
        while (nextSectionOptional.isPresent()) {
            Section nextSection = nextSectionOptional.get();
            stations.add(nextSection.downStation());
            nextSectionOptional = findByUpStation(nextSection.downStation());
        }
        return stations;
    }

    private Section firstSection() {
        return list.stream()
            .filter(this::doesNotHavePreviousSection)
            .findFirst()
            .orElseThrow(() -> new InvalidDataException("첫 구간이 존재하지 않습니다."));
    }

    private boolean doesNotHavePreviousSection(Section section) {
        return !findByDownStation(section.upStation()).isPresent();
    }

    private void removeOverlappingSection(Section section) {
        if (isExist(section.upStation())) {
            findByUpStation(section.upStation())
                .ifPresent(upSection -> upSection.remove(section));
            return;
        }
        findByDownStation(section.downStation())
            .ifPresent(downSection -> downSection.remove(section));
    }

    private void validateAddition(Section section) {
        boolean isExistUpStation = isExist(section.upStation());
        boolean isExistDownStation = isExist(section.downStation());

        if (isExistUpStation && isExistDownStation) {
            throw new DuplicateDataException(String.format("%s은 이미 등록된 구간 입니다.", section));
        }
        if (!isExistUpStation && !isExistDownStation) {
            throw new NotFoundException(String.format("%s은 등록할 수 없는 구간 입니다.", section));
        }
    }

    private boolean isExistByUpStation(Station station) {
        return upStationToSection().containsKey(station);
    }

    private boolean isExistByDownStation(Station station) {
        return downStationToSection().containsKey(station);
    }

    private Optional<Section> findByUpStation(Station station) {
        return Optional.ofNullable(
            upStationToSection().get(station)
        );
    }

    private Optional<Section> findByDownStation(Station station) {
        return Optional.ofNullable(
            downStationToSection().get(station)
        );
    }

    private boolean isExist(Station station) {
        return isExistByUpStation(station) || isExistByDownStation(station);
    }

    private boolean isNotExist(Station station) {
        return !isExist(station);
    }

    private Map<Station, Section> upStationToSection() {
        if (upStationToSectionCache == null) {
            upStationToSectionCache = list.stream()
                .collect(Collectors.toMap(Section::upStation, section -> section));
        }
        return upStationToSectionCache;
    }

    private Map<Station, Section> downStationToSection() {
        if (downStationToSectionCache == null) {
            downStationToSectionCache = list.stream()
                .collect(Collectors.toMap(Section::downStation, section -> section));
        }
        return downStationToSectionCache;
    }

    private void deleteSectionCaches() {
        downStationToSectionCache = null;
        upStationToSectionCache = null;
    }
}
