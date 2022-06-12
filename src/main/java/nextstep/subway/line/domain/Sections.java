package nextstep.subway.line.domain;

import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static nextstep.subway.error.ErrorCode.EXISTS_BOTH_STATIONS;
import static nextstep.subway.error.ErrorCode.NO_EXISTS_BOTH_STATIONS;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return emptyList();
        }
        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        while (upStation != null) {
            stations.add(upStation);
            upStation = getDownStation(upStation);
        }
        return stations;
    }

    public Station findUpStation() {
        if (sections.isEmpty()) {
            return null;
        }
        Station downStation = sections.get(0).getUpStation();
        Station tempStation = downStation;
        while (downStation != null) {
            tempStation = downStation;
            downStation = getUpStation(downStation);
        }
        return tempStation;
    }

    private Station getUpStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.matchDownStation(downStation))
                .findFirst()
                .map(Section::getUpStation)
                .orElse(null);
    }

    private Station getDownStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.matchUpStation(upStation))
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::matchUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::matchDownStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new ErrorCodeException(EXISTS_BOTH_STATIONS);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new ErrorCodeException(NO_EXISTS_BOTH_STATIONS);
        }

        if (isUpStationExisted) {
            updateUpStation(section);
            sections.add(section);
            return;
        }
        updateDownStation(section);
        sections.add(section);
    }

    private void updateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.matchUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.matchDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public void remove(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.matchUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.matchDownStation(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    public List<Section> getSections() {
        return sections;
    }
}
