package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        this.sections.add(section);
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

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        duplicateCheck(isUpStationExisted(upStation, stations), isDownStationExisted(downStation, stations));
        validateCheck(upStation, downStation, stations);
        addNewSection(line, upStation, downStation, stations, distance);
    }

    private void duplicateCheck(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateCheck(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void addNewSection(Line line, Station upStation, Station downStation, List<Station> stations, int distance) {
        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        if (isUpStationExisted(upStation, stations)) {
            updateUpStation(line, upStation, downStation, distance);
            return;
        }
        if (isDownStationExisted(downStation, stations)) {
            updateDownStation(line, upStation, downStation, distance);
            return;
        }
        throw new RuntimeException("등록할 수 없는 구간 입니다.");
    }

    private boolean isUpStationExisted(Station upStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private boolean isDownStationExisted(Station downStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == downStation);
    }


    private void updateUpStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void updateDownStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }


}
