package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.exception.InvalidEntityValueException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public void add(Section section) {
        List<Station> stations = getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(downStation));

        validateDuplication(isUpStationExisted, isDownStationExisted);
        validateSection(stations, upStation, downStation);

        if(stations.isEmpty()) {
            addSection(section.getLine(), upStation, downStation, section.getDistance());
            return;
        }

        if(isUpStationExisted) {
            updateSectionAsUpStation(section, upStation, downStation);
            addSection(section.getLine(), upStation, downStation, section.getDistance());
            return;
        }

        if(isDownStationExisted) {
            updateSectionAsDownStation(section, upStation, downStation);
            addSection(section.getLine(), upStation, downStation, section.getDistance());
            return;
        }
    }

    public void remove(Station station) {
        if(sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findStationAsUpStation(station);
        Optional<Section> downLineStation = findStationAsDownStation(station);

        if(upLineStation.isPresent() && downLineStation.isPresent()) {
            Line line = downLineStation.get().getLine();
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();

            addSection(line, newUpStation, newDownStation, newDistance);
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        if(sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        stations.add(station);

        Optional<Section> nextLineStation = findStationAsUpStation(station);
        while(nextLineStation.isPresent()) {
            station = nextLineStation.get().getDownStation();
            stations.add(station);

            nextLineStation = findStationAsUpStation(station);
        }

        return stations;
    }

    private Station findUpStation() {
        Station station = sections.get(0).getUpStation();

        Optional<Section> nextLineStation = findStationAsDownStation(station);
        while(nextLineStation.isPresent()) {
            station = nextLineStation.get().getUpStation();
            nextLineStation = findStationAsDownStation(station);
        }

        return station;
    }

    private Optional<Section> findStationAsUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToUpStation(station))
            .findFirst();
    }

    private Optional<Section> findStationAsDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToDownStation(station))
            .findFirst();
    }

    private void validateSection(List<Station> stations, Station upStation, Station downStation) {
        if(!stations.isEmpty()
            && stations.stream().noneMatch(it -> it.equals(upStation))
            && stations.stream().noneMatch(it -> it.equals(downStation))) {

            throw new InvalidEntityValueException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateDuplication(boolean isUpStationExisted, boolean isDownStationExisted) {
        if(isUpStationExisted && isDownStationExisted) {
            throw new InvalidEntityValueException("이미 등록된 구간 입니다.");
        }
    }

    private void addSection(Line line, Station upStation, Station downStation, int distance) {
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void updateSectionAsUpStation(Section section, Station upStation, Station downStation) {
        Optional<Section> stationAsUpStation = findStationAsUpStation(upStation);
        stationAsUpStation.ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));
    }

    private void updateSectionAsDownStation(Section section, Station upStation, Station downStation) {
        Optional<Section> stationAsDownStation = findStationAsDownStation(downStation);
        stationAsDownStation.ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));
    }
}
