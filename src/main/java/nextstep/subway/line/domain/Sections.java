package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MINIMUM_DELETE_SECTION_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (!sections.isEmpty()) {
            updateSection(section);
        }
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstUpStation();
        stations.add(downStation);

        while (sections.size() + 1 != stations.size()) {
            downStation = findNextLineStation(downStation).getDownStation();
            stations.add(downStation);
        }

        return Collections.unmodifiableList(stations);
    }

    public void deleteSection(Station station, Line line) {
        if (sections.size() <= MINIMUM_DELETE_SECTION_SIZE) {
            throw new IllegalArgumentException("마지막 구간은 삭제할 수 없음");
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> station.equals(it.getUpStation()))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> station.equals(it.getDownStation()))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private Station findFirstUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(this::isStartStation)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Section findNextLineStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> finalDownStation.equals(it.getUpStation()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isStartStation(Station station) {
        return sections.stream()
                .noneMatch(currentStation -> station.equals(currentStation.getDownStation()));
    }

    private void updateSection(Section section) {
        if (isContainAllStation(section)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        Section targetSection = findTargetSection(section);
        targetSection.changeStation(section);
    }

    private boolean isContainAllStation(Section section) {
        return sections.stream()
                .anyMatch(currentSection -> currentSection.equals(section));
    }

    private Section findTargetSection(Section section) {
        return sections.stream()
                .filter(currentSection -> currentSection.hasAnyStations(section))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상행역과 하행역 둘 중 하나도 포함되어 있지 않으면 추가할 수 없음"));
    }
}
