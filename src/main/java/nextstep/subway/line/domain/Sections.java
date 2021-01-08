package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(){}

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean add(Section section) {
        Stations stations = new Stations(getStations());
        if (stations.isEmpty()) {
            return sections.add(section);
        }
        validateSection(stations, section);

        if (stations.contains(section.getUpStation())) {
            updateUpStationInSections(section);
            return sections.add(section);
        }

        updateDownStationInSections(section);
        return sections.add(section);
    }

    public boolean remove(Section section) {
        return sections.remove(section);
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

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            mergeSection(upLineStation.get(), downLineStation.get());
        }
    }

    private void validateSection(Stations stations, Section section) {
        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void updateUpStationInSections(Section section) {
        updateStationInSections(it -> it.equalsUpStation(section.getUpStation()),
                it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStationInSections(Section section) {
        updateStationInSections(it -> it.equalsDownStation(section.getDownStation()),
                it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateStationInSections(Predicate<Section> filter, Consumer<Section> updater) {
        sections.stream()
                .filter(filter)
                .findFirst()
                .ifPresent(updater);
    }

    private void mergeSection(Section upSection, Section downSection) {
        Line line = sections.get(0).getLine();

        int newDistance = upSection.getDistance() + downSection.getDistance();
        add(new Section(line, upSection.getUpStation(), downSection.getDownStation(), newDistance));
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
}
