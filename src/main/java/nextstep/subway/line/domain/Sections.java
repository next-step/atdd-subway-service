package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (this.sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        boolean isUpStationExisted = findByStation(section.getUpStation()).isPresent();
        boolean isDownStationExisted = findByStation(section.getDownStation()).isPresent();

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }

        this.addSection(section, isUpStationExisted, isDownStationExisted);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeLineStation(Station station, Line line) {
        validateDeleteStationInSection();

        Optional<Section> upLineStation = findByUpStation(station);
        Optional<Section> downLineStation = findByDownStation(station);

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().merge(downLineStation.get().getDistance());
            this.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void addSection(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isDownStationExisted && !isUpStationExisted) {
            throw new IllegalStateException();
        }

        if (isUpStationExisted) {
            findByUpStation(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }

        if (isDownStationExisted) {
            findByDownStation(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }

        this.sections.add(section);
    }

    private void validateDeleteStationInSection() {
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException();
        }
    }

    private Station findUpStation() {
        Section startSection = findStartSection(this.sections.get(0));
        return startSection.getUpStation();
    }

    private Section findStartSection(Section section) {
        Optional<Section> optionalSection = findByDownStation(section.getUpStation());
        return optionalSection.map(this::findStartSection).orElse(section);
    }

    private Optional<Section> findByUpStation(Station station) {
        return this.sections.stream()
            .filter(section -> section.containUpStation(station))
            .findFirst();
    }

    private Optional<Section> findByDownStation(Station station) {
        return this.sections.stream()
            .filter(section -> section.containDownStation(station))
            .findFirst();
    }

    private Optional<Section> findByStation(Station station) {
        return this.sections.stream()
            .filter(section -> section.containUpStation(station) || section.containDownStation(station))
            .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

}
