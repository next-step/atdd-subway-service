package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

@Embeddable
public class LineSections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {

        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = toStations();

        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isUpStationExisted = isExistedStation(stations, upStation);
        boolean isDownStationExisted = isExistedStation(stations, downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new AddSectionException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new AddSectionException("등록할 수 없는 구간 입니다.");
        }

        int distance = section.getDistance();

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(section);
            return;
        }

        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        sections.add(section);
    }

    private boolean isExistedStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public List<Station> toStations() {

        if (CollectionUtils.isEmpty(sections)) {
            return Collections.emptyList();
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

    public void delete(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new DeleteSectionException("남은 구간이 1개이면 삭제할 수 없습니다.");
        }

        Optional<Section> upLineStation = sections.stream()
                                                  .filter(it -> it.getUpStation() == station)
                                                  .findFirst();
        Optional<Section> downLineStation = sections.stream()
                                                    .filter(it -> it.getDownStation() == station)
                                                    .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }
}
