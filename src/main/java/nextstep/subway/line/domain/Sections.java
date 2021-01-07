package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidAddSectionException;
import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.line.exception.SectionNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        checkExistSection(section);
        checkContainAnyStation(section);

        addSectionUpToUp(section);
        addSectionDownToDown(section);

        sections.add(Section.of(section));
    }

    public void removeSection(Station station) {
        checkSectionsSize();

        Optional<Section> upLineStation = findSection(section -> section.isEqualUpStationTo(station));
        Optional<Section> downLineStation = findSection(section -> section.isEqualDownStationTo(station));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section preSection = downLineStation.get();
            Section nextSection= upLineStation.get();

            sections.add(Section.builder()
                    .line(preSection.getLine())
                    .upStation(preSection.getUpStation())
                    .downStation(nextSection.getDownStation())
                    .distance(plusDistance(preSection, nextSection))
                    .build());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Section firstSection = findFirstSection();
        stations.add(firstSection.getUpStation());

        Section nextSection = firstSection;
        while (isNotNull(nextSection)) {
            stations.add(nextSection.getDownStation());
            nextSection = findNextSection(nextSection.getDownStation()).orElse(null);
        }
        return stations;
    }

    private void checkExistSection(Section section) {
        if (isExistStation(section.getUpStation()) && isExistStation(section.getDownStation())) {
            throw new InvalidAddSectionException("이미 등록된 구간 입니다.");
        }
    }

    private void checkContainAnyStation(Section section) {
        if (!isExistStation(section.getUpStation()) && !isExistStation(section.getDownStation())) {
            throw new InvalidAddSectionException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isExistStation(Station station) {
        return getStations().contains(station);
    }

    private void addSectionUpToUp(Section section) {
        findSection(it -> it.isEqualUpStation(section))
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void addSectionDownToDown(Section section) {
        findSection(it -> it.isEqualDownStation(section))
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void checkSectionsSize() {
        if (sections.size() <= 1) {
            throw new InvalidRemoveSectionException("지하철역이 2개 등록되어 있어서 역을 제거할 수 없습니다.");
        }
    }

    private boolean isNotNull(Section section) {
        return section != null;
    }

    private Section findFirstSection() {
        List<Station> downStations = getDownStations();

        return findSection(section -> !downStations.contains(section.getUpStation()))
                .orElseThrow(() -> new SectionNotFoundException("첫번째 Section을 찾을 수가 없습니다."));
    }

    private Optional<Section> findNextSection(Station downStation) {
        return findSection(section -> section.isEqualUpStationTo(downStation));
    }

    private Optional<Section> findSection(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findFirst();
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private Distance plusDistance(Section preSection, Section nextSection) {
        return preSection.getDistance().plusDistance(nextSection.getDistance());
    }
}
