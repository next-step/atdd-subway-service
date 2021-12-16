package nextstep.subway.line.domain;

import nextstep.subway.line.exception.AlreadyAddSectionException;
import nextstep.subway.line.exception.ExistsOnlyOneSectionInLineException;
import nextstep.subway.line.exception.NotExistStationInLineException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    private static final Integer ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        sections.add(section);
    }

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(newSection::equalsUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(newSection::equalsDownStation);

        checkExistsSection(isUpStationExisted, isDownStationExisted);
        checkNotAddSection(newSection, stations);

        if (stations.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            addFrontSection(newSection);
            return;
        }

        if (isDownStationExisted) {
            addBackSection(newSection);
        }
    }

    private void addBackSection(Section newSection) {
        this.sections.stream()
                .filter(section -> section.equalsDownStation(newSection.getDownStation()))
                .findFirst()
                .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

        this.sections.add(newSection);
    }

    private void addFrontSection(Section newSection) {
        this.sections.stream()
                .filter(section -> section.equalsUpStation(newSection.getUpStation()))
                .findFirst()
                .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        this.sections.add(newSection);
    }

    private void checkNotAddSection(Section newSection, List<Station> stations) {
        if (!stations.isEmpty() && notExistsStation(newSection, stations)) {
            throw new NotExistStationInLineException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean notExistsStation(Section newSection, List<Station> stations) {
        return stations.stream().noneMatch(newSection::equalsUpStation) && stations.stream().noneMatch(newSection::equalsDownStation);
    }

    private void checkExistsSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new AlreadyAddSectionException("이미 등록된 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(section -> section.equalsUpStation(finalDownStation))
                    .findFirst();
            downStation = addDownStation(stations, nextLineStation);
        }

        return stations;
    }

    private Station findUpStation() {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();
        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        upStations.removeAll(downStations);
        Station firstStation = upStations.get(0);
        return sections.stream()
                .filter(section -> section.getUpStation().equals(firstStation))
                .findAny()
                .get()
                .getUpStation();
    }

    public void removeSection(Line line, Station station) {
        existOnlyOneSection();

        Optional<Section> upLineStation = this.sections.stream()
                .filter(section -> section.equalsUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(section -> section.equalsDownStation(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(section -> this.sections.remove(section));
        downLineStation.ifPresent(section -> this.sections.remove(section));
    }

    private void existOnlyOneSection() {
        if (this.sections.size() <= ONE) {
            throw new ExistsOnlyOneSectionInLineException("구간이 두개 이상이어야 삭제가 가능합니다");
        }
    }

    private Station addDownStation(List<Station> stations, Optional<Section> nextLineStation) {
        if (!nextLineStation.isPresent()) {
            return null;
        }
        Station downStation = nextLineStation.get().getDownStation();
        stations.add(downStation);
        return downStation;
    }

    public Distance getTotalDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(new Distance(0), Distance::plus);
    }
}
