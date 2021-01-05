package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(final List<Section> sections) {
        this.sections.addAll(sections);
    }

    public List<Station> getOrderedStations() {
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
                .orElseThrow(IllegalStateException::new);
    }

    private boolean isUpStation(final Station station) {
        return sections.stream()
                .noneMatch(section -> station == section.getDownStation());
    }

    private void addOrderedStations(final List<Station> stations, Station baseStation) {
        stations.add(baseStation);
        List<Section> targets = new ArrayList<>(sections);
        Station next = baseStation;
        while (targets.size() > 0) {
            Section section = nextTarget(targets, next);
            Station downStation = section.getDownStation();
            stations.add(downStation);
            next = downStation;
        }
    }

    private Section nextTarget(final List<Section> targets, final Station next) {
        Section section = targets.stream()
                .filter(target -> next == target.getUpStation())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        targets.remove(section);
        return section;
    }

    public void add(final Section section) {
        boolean isUpStationExisted = contains(section.getUpStation());
        boolean isDownStationExisted = contains(section.getDownStation());

        addValidate(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            separateUp(section);
        }

        if (isDownStationExisted) {
            separateDown(section);
        }

        sections.add(section);
    }

    private void separateDown(final Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void separateUp(final Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void addValidate(final boolean isUpStationExisted, final boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean contains(final Station station) {
        return getOrderedStations().stream()
                .anyMatch(it -> it == station);
    }

    public void remove(final Station station) {
        removeValidate();
        Optional<Section> upSection = finnByUpStation(station);
        Optional<Section> downSection = findByDownStation(station);

        if (canRemove(upSection.isPresent(), downSection.isPresent())) {
            Line line = sections.get(0).getLine();
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    private boolean canRemove(final boolean upStation, final boolean downStation) {
        return upStation && downStation;
    }

    private Optional<Section> findByDownStation(final Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> finnByUpStation(final Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void removeValidate() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
