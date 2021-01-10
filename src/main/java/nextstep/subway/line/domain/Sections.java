package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {
    @Transient
    private static final int MIN_SECTION_SIZE = 1;
    @Transient
    private static final int MIN_SIZE = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(final List<Section> sections) {
        this.sections.addAll(sections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        addOrderedStations(stations, upStation);
        return stations;
    }

    private Station findUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(this::isUpStation)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    private boolean isUpStation(Station station) {
        return sections.stream()
                .noneMatch(section -> station == section.getDownStation());
    }

    private void addOrderedStations(List<Station> stations, Station baseStation) {
        stations.add(baseStation);
        List<Section> sections = new ArrayList<>(this.sections);
        Station nextStation = baseStation;
        while (sections.size() > MIN_SIZE) {
            Section section = nextSection(sections, nextStation);
            Station downStation = section.getDownStation();
            stations.add(downStation);
            nextStation = downStation;
        }
    }

    private Section nextSection(List<Section> sections, Station nextStation) {
        Section nextSection = sections.stream()
                .filter(section -> nextStation == section.getUpStation())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        sections.remove(nextSection);
        return nextSection;
    }

    public void addSection(Section section) {
        boolean isUpStationExisted = containStation(section.getUpStation());
        boolean isDownStationExisted = containStation(section.getDownStation());

        validateAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            separateUpStation(section);
        }

        if (isDownStationExisted) {
            separateDownStation(section);
        }

        sections.add(section);
    }

    public void removeSection(Line line, Station station) {
        validateRemoveSection();

        Optional<Section> upSection = findByUpStation(station);
        Optional<Section> downSection = findByDownStation(station);
        validateRemoveStation(upSection.isPresent(), downSection.isPresent());

        if(canRemove(upSection.isPresent(), downSection.isPresent())) {
            removeMiddleStation(line, upSection.get(), downSection.get());
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    private boolean canRemove(boolean upStation, boolean downStation) {
        return upStation && downStation;
    }

    private void removeMiddleStation(Line line, Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        Distance newDistance = upSection.getDistance().addDistance(downSection.getDistance());
        sections.add(Section.builder().line(line)
                .upStation(newUpStation)
                .downStation(newDownStation)
                .distance(newDistance)
                .build());
    }

    private Optional<Section> findByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isMatchUpStation(station))
                .findFirst();
    }

    private Optional<Section> findByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isMatchDownStation(station))
                .findFirst();
    }

    private boolean containStation(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
    }

    private void separateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void separateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void validateAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateRemoveSection() {
        if(sections.size() <= MIN_SECTION_SIZE) {
            throw new IllegalArgumentException("구간이 하나인 지하철 노선은 구간을 제거할 수 없습니다.");
        }
    }

    private void validateRemoveStation(boolean upStation, boolean downStation) {
        if (!upStation && !downStation) {
            throw new IllegalArgumentException("노선에 등록되지 않은 역은 제거할 수 없습니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
