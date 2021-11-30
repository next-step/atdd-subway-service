package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Sections
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    private boolean isExistStation(Station station) {
        return getStations().stream().anyMatch(it -> it.equals(station));
    }

    public void add(Line line, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = isExistStation(upStation);
        boolean isDownStationExisted = isExistStation(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!getStations().isEmpty() && getStations().stream().noneMatch(it -> it.equals(upStation)) &&
                getStations().stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (line.getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            line.getSections().stream()
                    .filter(it -> it.getUpStation().equals(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            line.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    public void remove(Line line, Long stationId) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = getSectionInUpStationByStationId(stationId);
        Optional<Section> downLineStation = getSectionInDownStationByStationId(stationId);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> getSectionInDownStationByStationId(Long stationId) {
        return sections.stream()
                .filter(it -> it.getDownStation().getId().equals(stationId))
                .findFirst();
    }

    private Optional<Section> getSectionInUpStationByStationId(Long stationId) {
        return sections.stream()
                .filter(it -> it.getUpStation().getId().equals(stationId))
                .findFirst();
    }

    public List<Section> getList() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (Optional.ofNullable(downStation).isPresent()) {
            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = sections.stream()
//                    .filter(it -> it.getUpStation().equals(finalDownStation))
//                    .findFirst();
            Optional<Section> nextLineStation = getSectionInUpStationByStationId(finalDownStation.getId());

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
        while (Optional.ofNullable(downStation).isPresent()) {
            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = sections.stream()
//                    .filter(it -> it.getDownStation().equals(finalDownStation))
//                    .findFirst();
            Optional<Section> nextLineStation = getSectionInDownStationByStationId(finalDownStation.getId());
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

}
