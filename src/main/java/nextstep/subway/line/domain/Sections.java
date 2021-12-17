package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.BadRequestApiException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    public static Sections empty() {
        return new Sections();
    }

    public boolean addSection(Section section) {
        if (!sections.isEmpty()) {
            validate(section);
            updateSection(section);
        }
        return sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean hasSectionByUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualUpStation(station));
    }

    public boolean hasSectionByDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualDownStation(station));
    }

    public boolean hasDeletableSection() {
        return sections.size() > 1;
    }

    public Section getSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("상행역이 일치하는 구간이 없습니다. (upStationId: %d)", station.getId())));
    }

    public Section getSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("하행역이 일치하는 구간이 없습니다. (downStationId: %d)", station.getId())));
    }

    public void deleteByUpStation(Station station) {
        sections.stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst()
                .ifPresent(findSection -> sections.remove(findSection));
    }

    public void deleteByDownStation(Station station) {
        sections.stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst()
                .ifPresent(findSection -> sections.remove(findSection));
    }

    public void deleteByBetweenStation(Station station) {
        Section oldSection = getSectionByDownStation(station);
        Section nextOldSection = getSectionByUpStation(station);
        Line line = oldSection.getLine();

        Station newUpStation = oldSection.getUpStation();
        Station newDownStation = nextOldSection.getDownStation();
        int newDistance = oldSection.getDistance() + nextOldSection.getDistance();
        Section newSection = Section.of(line, newUpStation, newDownStation, newDistance);

        sections.removeAll(Arrays.asList(oldSection, nextOldSection));
        sections.add(newSection);
    }

    public Section getFirstSection() {
        if (sections.isEmpty()) {
            throw new NoSuchElementException("구간 목록이 비어있습니다.");
        }
        return sections.get(0);
    }

    private Stations getStations() {
        Stations stations = Stations.empty();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private void validate(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Stations stations = getStations();

        boolean isUpStationExisted = stations.contains(upStation);
        boolean isDownStationExisted = stations.contains(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new BadRequestApiException(ErrorCode.DUPLICATE_SECTION);
        }
        if (stations.isNotEmpty() && stations.notContains(upStation) && stations.notContains(downStation)) {
            throw new BadRequestApiException(ErrorCode.CAN_NOT_ADD_SECTION);
        }
    }

    private void updateSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        if (getStations().contains(upStation)) {
            sections.stream()
                    .filter(it -> it.getUpStation().equals(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
        }
        if (getStations().contains(downStation)) {
            sections.stream()
                    .filter(it -> it.getDownStation().equals(downStation))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }
}
