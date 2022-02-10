package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Optional<Section> upwardSection = upwardSection(station);
        Optional<Section> downwardSection = downwardSection(station);
        if (upwardSection.isPresent() && downwardSection.isPresent()) {
            upwardSection.get().merge(downwardSection.get());
        }
        upwardSection.ifPresent(it -> sections.remove(it));
        downwardSection.ifPresent(it -> sections.remove(it));
    }

    private void checkStationRemovable(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private Optional<Section> upwardSection(Station station) {
        return sections.stream()
            .filter(section -> section.isUpStation(station))
            .findFirst();
    }

    private Optional<Section> downwardSection(Station station) {
        return sections.stream()
            .filter(section -> section.isDownStation(station))
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


}
