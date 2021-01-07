package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Embeddable
public class Sections {

    private static final int MINIMUM_STATION_COUNT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        sections.add(new Section(line, upStation, downStation, distance));
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSectionByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
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
            Optional<Section> nextLineStation = getSectionByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void addLineStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        verifyAddLineStation(upStation, downStation, stations);

        if (addLineStationIfEmpty(line, upStation, downStation, distance, stations))
            return;

        updateUpStation(upStation, downStation, distance, stations);
        updateDownStation(upStation, downStation, distance, stations);

        addSection(line, upStation, downStation, distance);
    }

    public void removeLineStation(Line line, Station station) {
        verifyCannotRemove();

        Optional<Section> upLineSection = getSectionByUpStation(station);
        Optional<Section> downLineSection = getSectionByDownStation(station);

        replaceSectionByRemove(line, upLineSection, downLineSection);
        removeSection(upLineSection, downLineSection);
    }

    private Optional<Section> getSectionByUpStation(Station station) {
        return getSection(it -> it.sameUpStation(station));
    }

    private Optional<Section> getSectionByDownStation(Station station) {
        return getSection(it -> it.sameDownStation(station));
    }

    private Optional<Section> getSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    private boolean addLineStationIfEmpty(Line line, Station upStation, Station downStation, int distance,
                                          List<Station> stations) {
        if (stations.isEmpty()) {
            addSection(line, upStation, downStation, distance);
            return true;
        }
        return false;
    }

    private void updateUpStation(Station upStation, Station downStation, int distance, List<Station> stations) {
        if (isStationExisted(upStation, stations))
            updateStation(it -> it.sameUpStation(upStation), it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance, List<Station> stations) {
        if (isStationExisted(downStation, stations))
            updateStation(it -> it.sameDownStation(downStation), it -> it.updateDownStation(upStation, distance));
    }

    private boolean isStationExisted(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private void updateStation(Predicate<Section> sectionPredicate, Consumer<Section> sectionConsumer) {
        sections.stream()
                .filter(sectionPredicate)
                .findFirst()
                .ifPresent(sectionConsumer);
    }

    private void replaceSectionByRemove(Line line,
                                        Optional<Section> upLineOptSection, Optional<Section> downLineOptSection) {
        if (upLineOptSection.isPresent() && downLineOptSection.isPresent()) {
            Section downLineSection = downLineOptSection.get();
            Section upLineSection = upLineOptSection.get();

            addSection(line, downLineSection.getUpStation(), upLineSection.getDownStation(),
                    upLineSection.getDistance() + downLineSection.getDistance());
        }
    }

    private void removeSection(Optional<Section> upLineSection, Optional<Section> downLineSection) {
        upLineSection.ifPresent(it -> getSections().remove(it));
        downLineSection.ifPresent(it -> getSections().remove(it));
    }

    private void verifyAddLineStation(Station upStation, Station downStation, List<Station> stations) {
        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        verifyAlreadyExistSection(isUpStationExisted, isDownStationExisted);
        verifyNotMatchStations(isUpStationExisted, isDownStationExisted);
    }

    private void verifyNotMatchStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void verifyAlreadyExistSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void verifyCannotRemove() {
        if (sections.size() <= MINIMUM_STATION_COUNT) {
            throw new RuntimeException();
        }
    }
}
