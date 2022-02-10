package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        checkSection(section);
        sections.stream()
            .forEach(s -> s.addInnerSection(section));
        this.sections.add(section);
    }

    private void checkSection(Section section) {
        checkSectionDuplicate(section);
        checkStationExist(section);
    }

    private void checkSectionDuplicate(Section section) {
        sections.stream()
            .forEach(s -> s.checkSectionDuplicate(section));
    }

    private void checkStationExist(Section section) {
        List<Station> stations = getStations();
        if (!stations.contains(section.getUpStation()) &&
            !stations.contains(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeStation(Station station) {
        checkStationRemovable(station);
        if (upwardSection(station).isPresent() && downwardSection(station).isPresent()) {
            upwardSection(station).get().merge(downwardSection(station).get());
        }
        this.sections = sections.stream()
            .filter(section -> !section.hasStation(station))
            .collect(Collectors.toList());
    }

    private void checkStationRemovable(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException("노선은 두개의 역을 포함한 구간이 하나 이상 존재해야 합니다.");
        }
        if (!upwardSection(station).isPresent() && !downwardSection(station).isPresent()) {
            throw new RuntimeException("노선에 역이 포함되지 않습니다.");
        }
    }

    private Optional<Section> upwardSection(Station station) {
        return sections.stream()
            .filter(section -> section.isDownStation(station))
            .findFirst();
    }

    private Optional<Section> downwardSection(Station station) {
        return sections.stream()
            .filter(section -> section.isUpStation(station))
            .findFirst();
    }


    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstStation = findFirstStation();
        stations.add(firstStation);
        return Collections.unmodifiableList(addDownStations(firstStation, stations));
    }

    private List<Station> addDownStations(Station firstStation, List<Station> stations) {
        Station downStation = findDownStation(firstStation);
        if (downStation == null) {
            return stations;
        }
        stations.add(downStation);
        return addDownStations(downStation, stations);
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (true) {
            Station upStation = findUpStation(downStation);
            if (upStation == null) {
                break;
            }
            downStation = upStation;
        }
        return downStation;
    }

    private Station findUpStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getUpStation).orElse(null);
    }

    private Station findDownStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getDownStation).orElse(null);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
