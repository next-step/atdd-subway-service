package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public void add(Line line, Station upStation, Station downStation, int distance) {
        sections.add(new Section(line, upStation, downStation, distance));
    }

    public List<Section> getSections() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections;
    }

    public Station findUpStation() {
        Station upStation = sections.get(0).getUpStation();
        while (upStation != null) {
            Station finalUpStation = upStation;
            Optional<Section> findSection = sections.stream()
                    .filter(section -> section.getDownStation() == finalUpStation)
                    .findFirst();
            if (!findSection.isPresent()) {
                break;
            }
            upStation = findSection.get().getUpStation();
        }

        return upStation;
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
            Optional<Section> findSection = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!findSection.isPresent()) {
                break;
            }
            downStation = findSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        validateAllOrNothingSection(newSection.getUpStation(), newSection.getDownStation(), stations);

        ifStationsIsEmptyThenAddSection(newSection, stations);
    }

    private void ifStationsIsEmptyThenAddSection(Section newSection, List<Station> stations) {
        if (stations.isEmpty()) {
            sections.add(newSection);
            return;
        }
        addBetweenByUpStation(newSection, stations);
    }

    private void addBetweenByUpStation(Section newSection, List<Station> stations) {
        if (isAnyMatchInStations(stations, newSection.getUpStation())) {
            sections.stream()
                    .filter(section -> section.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(section -> section
                            .updateUpStation(newSection.getDownStation(), newSection.getDistance())
                    );

            sections.add(newSection);
        }
    }

    private boolean isAnyMatchInStations(List<Station> stations, Station findStation) {
        return stations.stream().anyMatch(station -> station == findStation);
    }

    private void validateAllOrNothingSection(Station upStation, Station downStation, List<Station> stations) {
        if (isAlreadyExistedSection(upStation, downStation, stations)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        if (isNotExistedSection(upStation, downStation, stations)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isNotExistedSection(Station upStation, Station downStation, List<Station> stations) {
        return !stations.isEmpty() && stations.stream()
                .noneMatch(station -> station == upStation) && stations.stream()
                .noneMatch(station -> station == downStation);
    }

    private boolean isAlreadyExistedSection(Station upStation, Station downStation,
                                            List<Station> stations) {
        return stations.stream()
                .anyMatch(station -> station == upStation) && stations.stream()
                .anyMatch(station -> station == downStation);
    }
}
