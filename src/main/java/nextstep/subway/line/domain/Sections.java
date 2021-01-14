package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateExist(section);
        replaceUpStation(section);
        replaceDownStation(section);
        sections.add(section);
    }

    private void replaceDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void replaceUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void validateExist(Section section) {
        List<Station> stations = getStations();
        if (sections.stream().anyMatch(it -> it.isSameUpStation(section.getUpStation()) && it.isSameDownStation(section.getDownStation()))) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        while (downStation != null) {
            stations.add(downStation);
            Section nextLineStation = findNextLineStationByUpStation(downStation);
            downStation = (nextLineStation != null) ? nextLineStation.getDownStation() : null;
        }

        return stations;
    }

    private Section findNextLineStationByUpStation(Station downStation) {
        return this.sections.stream()
                .filter(it -> it.getUpStation() == downStation)
                .findFirst()
                .orElse(null);
    }

    private Station findUpStation() {
        Section section = this.sections.stream().findFirst().orElseThrow(EntityNotFoundException::new);
        Station downStation = section.getUpStation();
        while (section != null) {
            downStation = section.getUpStation();
            section = findNextLineStationByDownStation(downStation);
        }
        return downStation;
    }

    private Section findNextLineStationByDownStation(Station downStation) {
        return this.sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .orElse(null);
    }

    public List<StationResponse> getStationResponses() {
        return getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void remove(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSectionAfterRemove(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void addSectionAfterRemove(Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        Line line = upSection.getLine();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }
}
