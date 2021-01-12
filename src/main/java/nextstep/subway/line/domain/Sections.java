package nextstep.subway.line.domain;

import nextstep.subway.advice.exception.SectionBadRequestException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void addSection(Station upStation, Station downStation, int distance, List<Station> stations, Line line) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validateSection(upStation, downStation, distance, stations, isUpStationExisted, isDownStationExisted);
        addSection(upStation, downStation, distance, stations, line, isUpStationExisted, isDownStationExisted);
    }

    public void remove(Station station, Line line) {
        if (this.getSections().size() <= 1) {
            throw new SectionBadRequestException("구간이 1개이하이면 삭제할 수 없습니다", station);
        }

        Optional<Section> upLineStation = this.getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        removeStation(upLineStation, downLineStation, line);
    }

    private void addSection(Station upStation, Station downStation, int distance, List<Station> stations, Line line, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (stations.isEmpty()) {
            this.getSections().add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.getSections().add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.getSections().add(new Section(line, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    private void removeStation(Optional<Section> upLineStation, Optional<Section> downLineStation, Line line) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.getSections().add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.getSections().remove(it));
        downLineStation.ifPresent(it -> this.getSections().remove(it));
    }

    private void validateSection(Station upStation, Station downStation, int distance, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionBadRequestException("이미 등록된 구간 입니다", upStation, downStation, distance);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new SectionBadRequestException("등록할 수 없는 구간 입니다", upStation, downStation, distance);
        }
    }


}
