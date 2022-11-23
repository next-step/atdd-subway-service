package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    private static final int ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        List<Station> stations = getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        boolean isUpStationExisted = isStationExist(upStation, stations);
        boolean isDownStationExisted = isStationExist(downStation, stations);

        verifyAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
            sections.add(new Section(section.getLine(), upStation, downStation, distance));
            return;
        }

        updateDownStation(upStation, downStation, distance);
        sections.add(new Section(section.getLine(), upStation, downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        getDownStationSection(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        getUpStationSection(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private static void verifyAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExist(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getUpStationSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeSection(Station station) {
        if (isSectionCountLessThanOne()) {
            throw new InvalidRemoveSectionException("구간이 1개인 노선은 구간을 삭제할 수 없습니다");
        }

        if (isLastStation(station)) {
            removeLastStation(station);
            return;
        }

        Section upLineStationSection = getUpStationSection(station).orElseThrow(InvalidRemoveSectionException::new);
        Section downLineStationSection = getDownStationSection(station).orElseThrow(InvalidRemoveSectionException::new);

        addSections(upLineStationSection, downLineStationSection);

        removeSections(upLineStationSection, downLineStationSection);
    }

    private void removeLastStation(Station station) {
        getUpStationSection(station).ifPresent(sections::remove);
        getDownStationSection(station).ifPresent(sections::remove);
    }

    private boolean isLastStation(Station station) {
        boolean isOnUpStation = getUpStationSection(station).isPresent();
        boolean isOnDownStation = getDownStationSection(station).isPresent();
        return (isOnUpStation && !isOnDownStation) || (!isOnUpStation && isOnDownStation);
    }

    private void removeSections(Section upLineStation, Section downLineStation) {
        sections.remove(upLineStation);
        sections.remove(downLineStation);
    }

    private void addSections(Section upLineStation, Section downLineStation) {
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(
                new Section(
                        upLineStation.getLine(),
                        downLineStation.getUpStation(),
                        upLineStation.getDownStation(),
                        newDistance));
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownStationSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> getDownStationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getUpStationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private boolean isSectionCountLessThanOne() {
        return sections.size() <= ONE;
    }

}
