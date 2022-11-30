package nextstep.subway.line.domain;

import com.google.common.collect.Lists;
import nextstep.subway.exception.DuplicatedSectionException;
import nextstep.subway.exception.EmptySectionException;
import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createEmpty() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        checkUniqueSection(section);
        checkValidSection(section);
        updateUpStation(section);
        updateDownStation(section);
        sections.add(section);
    }

    private void checkUniqueSection(Section section) {
        if (this.sections.contains(section)) {
            throw new DuplicatedSectionException(ExceptionMessage.DUPLICATED_SECTION);
        }
    }

    private void checkValidSection(Section section) {
        if (sections.isEmpty()) {
            return;
        }

        if (!hasAnyMatchedStation(section)) {
            throw new InvalidSectionException(ExceptionMessage.INVALID_SECTION);
        }
    }

    private void updateUpStation(Section section) {
        boolean isUpStationExisted = sections.stream().anyMatch(it -> it.hasAnyMatchedThisUpStation(section));

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(it -> it.hasSameUpStation(section))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section));
        }
    }

    private void updateDownStation(Section section) {
        boolean isDownStationExisted = sections.stream().anyMatch(it -> it.hasAnyMatchedThisDownStation(section));

        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(it -> it.hasSameDownStation(section))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section));
        }
    }

    private boolean hasAnyMatchedStation(Section section) {
        return sections.stream().anyMatch(it -> it.hasAnyMatchedStation(section));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(this.sections);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        Station currentStation = findFirstStationOfLine();
        List<Station> stations = Lists.newArrayList(currentStation);

        Optional<Station> nextStation = findNextStation(currentStation);

        while (nextStation.isPresent()) {
            currentStation = nextStation.get();
            stations.add(currentStation);
            nextStation = findNextStation(currentStation);
        }

        return stations;
    }

    private Station findFirstStationOfLine() {
        Station currentUpStation = sections.get(0).getUpStation();
        Optional<Station> prevStation = findPrevStation(currentUpStation);

        while (prevStation.isPresent()) {
            currentUpStation = prevStation.get();
            prevStation = findPrevStation(currentUpStation);
        }

        return currentUpStation;
    }

    private Optional<Station> findPrevStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(upStation))
                .findFirst()
                .map(Section::getUpStation);
    }

    private Optional<Station> findNextStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(downStation))
                .findFirst()
                .map(Section::getDownStation);
    }

    public void remove(Station station) {
        checkValidRemovableStatus();

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.add(new Section(downLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

    }

    private void checkValidRemovableStatus() {
        if (this.sections.size() <= 1) {
            throw new EmptySectionException(ExceptionMessage.EMPTY_SECTION);
        }
    }

    public Lines findLinesFrom(List<Station> stations) {
        return Lines.of(sections.stream()
                .filter(it -> stations.contains(it.getUpStation()) && stations.contains(it.getDownStation()))
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toList()));
    }
}
