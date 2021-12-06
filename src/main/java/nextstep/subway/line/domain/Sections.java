package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected static Sections empty() {
        return new Sections();
    }

    public List<Station> getSortedStations() {
        if(sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    public void add(Section newSection) {
        List<Station> stations = getSortedStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.getUpStation()) &&
                stations.stream().noneMatch(it -> it == newSection.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

            sections.add(newSection);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == newSection.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

            sections.add(newSection);
        } else {
            throw new RuntimeException();
        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

}
