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

    public void addSection(Section section, List<Station> stations) {
        boolean isUpStationExisted = isStationExisted(stations, section.getUpStation());
        boolean isDownStationExisted = isStationExisted(stations, section.getDownStation());

        validateSection(section, stations, isUpStationExisted, isDownStationExisted);
        addSection(section, stations, isUpStationExisted, isDownStationExisted);
    }

    public void remove(Station station, Line line) {
        if (this.getSections().size() <= 1) {
            throw new SectionBadRequestException("구간이 1개이하이면 삭제할 수 없습니다", station);
        }

        Optional<Section> upLineStation = getUpSection(station);
        Optional<Section> downLineStation = getDownSection(station);

        removeStation(upLineStation, downLineStation, line);
    }

    private void addSection(Section section, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (stations.isEmpty()) {
            this.getSections().add(section);
            return;
        }

        if (isUpStationExisted) {
            addSectionIfUpStation(section);
        } else if (isDownStationExisted) {
            addSectionIfDownStation(section);
        } else {
            throw new SectionBadRequestException("잘못된 구간입니다.", section.getUpStation(), section.getDownStation(), section.getDistance());
        }
    }

    private void removeStation(Optional<Section> upLineStation, Optional<Section> downLineStation, Line line) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            connectRemovedSection(upLineStation, downLineStation, line);
        }
        upLineStation.ifPresent(it -> this.getSections().remove(it));
        downLineStation.ifPresent(it -> this.getSections().remove(it));
    }

    private void connectRemovedSection(Optional<Section> upLineStation, Optional<Section> downLineStation, Line line) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        this.getSections().add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void validateSection(Section section, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionBadRequestException("이미 등록된 구간 입니다", section.getUpStation(), section.getDownStation(), section.getDistance());
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new SectionBadRequestException("등록할 수 없는 구간 입니다", section.getUpStation(), section.getDownStation(), section.getDistance());
        }
    }

    private boolean isStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private void addSectionIfDownStation(Section section) {
        getDownSection(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        this.getSections().add(section);
    }

    private void addSectionIfUpStation(Section section) {
        getUpSection(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        this.getSections().add(section);
    }

    private Optional<Section> getUpSection(Station station) {
        return this.getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getDownSection(Station station) {
        return this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }


}
