package nextstep.subway.line.domain;

import nextstep.subway.line.application.CannotAddSectionException;
import nextstep.subway.line.application.CannotRemoveException;
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
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(Line line, Station upStation, Station downStation, int distance) {
        Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        validate(stations, isUpStationExisted, isDownStationExisted);

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            updateUpstation(upStation, downStation, distance);
            sections.add(new Section(line, upStation, downStation, distance));

        } else if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
            sections.add(new Section(line, upStation, downStation, distance));

        } else {
            throw new CannotAddSectionException("구간 등록 에러!! 상행역과 하행역을 잘 입력했는지 확인해 주세요.");
        }
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
            Optional<Section> nextLineStation = getSameUpstationSection(finalDownStation);
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
            Optional<Section> nextLineStation = getSameDownStationSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeLineStation(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new CannotRemoveException("구간이 하나일 경우 역을 삭제할 수 없습니다.");
        }

        Optional<Section> upLineStation = getSameUpstationSection(station);
        Optional<Section> downLineStation = getSameDownStationSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            connectPreStationAndNextStation(line, upLineStation, downLineStation);
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void connectPreStationAndNextStation(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private Optional<Section> getSameUpstationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getSameDownStationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void updateUpstation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void validate(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new CannotAddSectionException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new CannotAddSectionException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExisted(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }
}

