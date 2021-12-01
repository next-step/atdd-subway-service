package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
    @Transient
    public static final int MIN_SECTION_COUNT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Line line, Station upStation, Station downStation, Distance distance) {
        addValidate(upStation, downStation);

        if (getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isExistStation(upStation)) {
            line.getSections().stream()
                    .filter(it -> it.upStationEqualTo(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isExistStation(downStation)) {
            line.getSections().stream()
                    .filter(it -> it.downStationEqualTo(downStation))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        }
    }

    private void addValidate(Station upStation, Station downStation) {
        if (isExistStation(upStation) && isExistStation(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (isInvalidStations(upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isExistStation(Station station) {
        return getStations().stream().anyMatch(it -> it.equals(station));
    }

    private boolean isInvalidStations(Station upStation, Station downStation) {
        return !getStations().isEmpty() && !isExistStation(upStation) && !isExistStation(downStation);
    }

    public void remove(Line line, Long stationId) {
        removeValidate(stationId);

        Optional<Section> upLineSection = getSectionInUpStationByStationId(stationId);
        Optional<Section> downLineSection = getSectionInDownStationByStationId(stationId);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            Station newUpStation = downLineSection.get().getUpStation();
            Station newDownStation = upLineSection.get().getDownStation();
            Distance newDistance = Distance.valueOf(DistanceType.PLUS, upLineSection.get().getDistance(), downLineSection.get().getDistance());
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));

        }
        upLineSection.ifPresent(it -> sections.remove(it));
        downLineSection.ifPresent(it -> sections.remove(it));
    }

    private void removeValidate(Long stationId) {
        if (sections.size() <= MIN_SECTION_COUNT) {
            throw new RuntimeException();
        }

        if (getStations().stream().noneMatch(station -> station.getId().equals(stationId))) {
            throw new RuntimeException();
        }
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
            Optional<Section> nextLineStation = getSectionInDownStationByStationId(finalDownStation.getId());
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

}
