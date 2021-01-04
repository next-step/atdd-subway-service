package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidAddSectionException;
import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.line.exception.SectionNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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

        boolean isUpStationExisted = isExistStation(section.getUpStation());
        boolean isDownStationExisted = isExistStation(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new InvalidAddSectionException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new InvalidAddSectionException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else {
            throw new RuntimeException();
        }
    }

    public void removeSection(Station station) {
        checkSectionsSize();

        Optional<Section> upLineStation = findSection(section -> section.getUpStation() == station);
        Optional<Section> downLineStation = findSection(section -> section.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section preSection = downLineStation.get();
            Section nextSection= upLineStation.get();
            Line line = upLineStation.get().getLine();

            sections.add(Section.builder()
                    .line(line)
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

    public List<StationResponse> getStationResponses() {
        return getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private boolean isExistStation(Station station) {
        return getStations().contains(station);
    }

    private boolean isNotNull(Section section) {
        return section != null;
    }

    private void checkSectionsSize() {
        if (sections.size() <= 1) {
            throw new InvalidRemoveSectionException("지하철역이 2개 등록되어 있어서 역을 제거할 수 없습니다.");
        }
    }

    private Section findFirstSection() {
        List<Station> downStations = getDownStations();

        return findSection(section -> !downStations.contains(section.getUpStation()))
                .orElseThrow(() -> new SectionNotFoundException("첫번째 Section을 찾을 수가 없습니다."));
    }

    private Optional<Section> findNextSection(Station downStation) {
        return findSection(section -> section.getUpStation().equals(downStation));
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
