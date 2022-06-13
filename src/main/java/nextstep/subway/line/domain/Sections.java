package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getTargetToLastStations(findFirstStation(this.sections.get(0).getUpStation()));
    }

    public void addSection(Section section) {
        checkPossibleAddSection(section);

        if (isAddNewSection()) {
            this.sections.add(section);
            return;
        }
        if (isContainStationInSections(section.getUpStation())) {
            addSectionByUpToDown(section.getUpStation(), section.getDownStation(), section.getDistance());
        }
        if (isContainStationInSections(section.getDownStation())) {
            addSectionByMiddleToDown(section.getUpStation(), section.getDownStation(), section.getDistance());
        }

        this.sections.add(section);
    }

    public void removeStation(Line line, Station station) {
        if (this.sections.size() <= 1) {
            throw new RuntimeException("구간에 최소한 1개 이상의 구간이 존재해야 합니다.");
        }

        Section upSection = findSectionByPredicateAndRemove(section -> section.getUpStation() == station);
        Section downSection = findSectionByPredicateAndRemove(section -> section.getDownStation() == station);

        if (upSection != null && downSection != null) {
            Station newUpStation = downSection.getUpStation();
            Station newDownStation = upSection.getDownStation();
            Distance newDistance = upSection.getDistance().addThenReturnResult(downSection.getDistance());

            addSection(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    public List<Section> getValue() {
        return this.sections;
    }

    private void addStation(Line line, Station upStation, Station downStation, Distance distance) {
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    private Station findFirstStation(Station station) {
        Optional<Section> nextDownSection = this.sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();

        if (nextDownSection.isPresent()) {
            return findFirstStation(nextDownSection.get().getUpStation());
        }
        return station;
    }

    private List<Station> getTargetToLastStations(Station target) {
        List<Station> result = new ArrayList<>(Collections.singletonList(target));
        this.sections.stream()
                .filter(it -> it.getUpStation().equals(target))
                .findFirst()
                .ifPresent(section -> result.addAll(getTargetToLastStations(section.getDownStation())));

        return result;
    }

    private void checkPossibleAddSection(Section section) {
        boolean isUpStationExisted = isContainStationInSections(section.getUpStation());
        boolean isDownStationExisted = isContainStationInSections(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (isImpossibleAddSection(section.getUpStation(), section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isContainStationInSections(Station station) {
        return this.getStations()
                .stream()
                .anyMatch(it -> it.equals(station));
    }

    private boolean isNotContainStationInSections(Station station) {
        return !this.isContainStationInSections(station);
    }

    private boolean isImpossibleAddSection(Station upStation, Station downStation) {
        return this.isNotAddNewSection() &&
                this.isNotContainStationInSections(upStation) &&
                this.isNotContainStationInSections(downStation);
    }

    private boolean isAddNewSection() {
        return this.getStations().isEmpty();
    }

    private boolean isNotAddNewSection() {
        return !this.isAddNewSection();
    }

    private void addSectionByUpToDown(Station upStation, Station downStation, Distance distance) {
        this.sections.stream()
                .filter(it -> it.isSameUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void addSectionByMiddleToDown(Station upStation, Station downStation, Distance distance) {
        this.sections.stream()
                .filter(it -> it.isSameDownStation(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private Section findSectionByPredicateAndRemove(Predicate<Section> predicate) {
        Optional<Section> section = this.sections.stream()
                .filter(predicate)
                .findFirst();

        section.ifPresent(it -> this.sections.remove(it));

        return section.orElse(null);
    }
}
