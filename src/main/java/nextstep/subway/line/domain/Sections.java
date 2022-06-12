package nextstep.subway.line.domain;

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
    private List<Section> elements = new ArrayList<>();

    public Station lastUpStation() {
        Station upStation = elements.get(0).getUpStation();
        while (upStation != null) {
            Optional<Section> prevSection = findPrevSection(upStation);
            if (!prevSection.isPresent()) {
                break;
            }
            upStation = prevSection.get().getUpStation();
        }

        return upStation;
    }

    public List<Section> getElements() {
        return elements;
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = lastUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextSection = findNextSection(downStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public Optional<Section> findPrevSection(Station upStation) {
        return elements.stream()
                .filter(section -> section.getDownStation() == upStation)
                .findFirst();
    }

    public Optional<Section> findNextSection(Station downStation) {
        return elements.stream()
                .filter(section -> section.getUpStation() == downStation)
                .findFirst();
    }

    public Optional<Section> findSectionByUpStationSameAs(Station station) {
        return elements.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> findSectionByDownStationSameAs(Station station) {
        return elements.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public void add(Section section) {
        validateStations(section.getUpStation(), section.getDownStation());

        rearrangeElementsFor(section);

        elements.add(section);
    }

    private void rearrangeElementsFor(Section section) {
        elements.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> {
                    it.updateUpStation(section.getDownStation(), section.getDistance());
                    return;
                });

        elements.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> {
                    it.updateDownStation(section.getUpStation(), section.getDistance());
                    return;
                });
    }

    private void validateStations(Station upStation, Station downStation) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeStation(Station station) {
        if (elements.size() <= 1) {
            throw new IllegalStateException("구간이 하나뿐일 때는 삭제할 수 없습니다.");
        }

        Optional<Section> upSection = findSectionByDownStationSameAs(station);
        Optional<Section> downSection = findSectionByUpStationSameAs(station);

        if (downSection.isPresent() && upSection.isPresent()) {
            Station newUpStation = upSection.get().getUpStation();
            Station newDownStation = downSection.get().getDownStation();
            int newDistance = downSection.get().getDistance() + upSection.get().getDistance();
            elements.add(new Section(upSection.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        downSection.ifPresent(it -> elements.remove(it));
        upSection.ifPresent(it -> elements.remove(it));
    }
}
