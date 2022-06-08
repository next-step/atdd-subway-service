package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getFromToLastStations(findUpStation());
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        checkPossibleAddSection(upStation, downStation);

        boolean isUpStationExisted = isContainStationInSections(upStation);
        boolean isDownStationExisted = isContainStationInSections(downStation);

        if (isAddNewSection()) {
            addSection(line, upStation, downStation, distance);
            return;
        }

        if (isUpStationExisted) {
            addSectionByUpToDown(upStation, downStation, distance);

            addSection(line, upStation, downStation, distance);
            return;
        }
        if (isDownStationExisted) {
            addSectionByMiddleToDown(upStation, downStation, distance);

            addSection(line, upStation, downStation, distance);
            return;
        }

        throw new RuntimeException();
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    public void removeStation(Line line, Station station) {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }

        Section upSection = findSectionByPredicateAndRemove(section -> section.getUpStation() == station);
        Section downSection = findSectionByPredicateAndRemove(section -> section.getDownStation() == station);

        if (upSection != null && downSection != null) {
            Station newUpStation = downSection.getUpStation();
            Station newDownStation = upSection.getDownStation();
            int newDistance = upSection.getDistance() + downSection.getDistance();

            addSection(line, newUpStation, newDownStation, newDistance);
        }
    }

    public List<Section> getSections() {
        return this.sections;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();

        return findStationByPredicateWithExecuteAction(
                downStation,
                null,
                (section, station) -> section.getDownStation() == station,
                Section::getUpStation
        );
    }

    private List<Station> getFromToLastStations(Station startStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(startStation);

        findStationByPredicateWithExecuteAction(
                startStation,
                stations::add,
                (section, station) -> section.getUpStation() == station,
                Section::getDownStation
        );

        return stations;
    }

    private Station findStationByPredicateWithExecuteAction(
            Station firstStation, Consumer<Station> action,
            BiPredicate<Section, Station> findNextFilter,
            Function<Section, Station> findNextTarget
    ) {
        boolean isEnd = false;
        Station downStation = firstStation;

        while (!isEnd) {
            Station finalDownStation = downStation;
            Section nextSection = this.sections.stream()
                    .filter(it -> findNextFilter.test(it, finalDownStation))
                    .findFirst()
                    .orElse(null);

            isEnd = (nextSection == null);
            downStation = findNextStationAndAfterProcessing(downStation, nextSection, action, findNextTarget);
        }

        return downStation;
    }

    private Station findNextStationAndAfterProcessing (
            Station beforeProcessingStation, Section nextSection,
            Consumer<Station> action, Function<Section, Station> findNextTarget
    ) {
        if (nextSection == null) {
            return beforeProcessingStation;
        }

        Station downStation = findNextTarget.apply(nextSection);

        if (action != null) {
            action.accept(downStation);
        }

        return downStation;
    }

    private void checkPossibleAddSection(Station upStation, Station downStation) {
        boolean isUpStationExisted = isContainStationInSections(upStation);
        boolean isDownStationExisted = isContainStationInSections(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (isImpossibleAddSection(upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isContainStationInSections(Station station) {
        return this.getStations()
                .stream()
                .anyMatch(it -> it == station);
    }

    private boolean isImpossibleAddSection(Station upStation, Station downStation) {
        return !this.getStations().isEmpty() &&
                this.getStations().stream().noneMatch(it -> it == upStation) &&
                this.getStations().stream().noneMatch(it -> it == downStation);
    }

    private boolean isAddNewSection() {
        return this.getStations()
                .isEmpty();
    }

    private void addSectionByUpToDown(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void addSectionByMiddleToDown(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getDownStation() == downStation)
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
