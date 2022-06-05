package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            updateSection(section);
        }
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
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

    private void updateSection(Section newSection) {
        sections.stream()
                .filter(section -> section.getUpStation().equals(newSection.getUpStation()))
                .findFirst()
                .ifPresent(section -> section.updateUpStation(newSection));

        sections.stream()
                .filter(it -> it.getDownStation().equals(newSection.getDownStation()))
                .findFirst()
                .ifPresent(section -> section.updateDownStation(newSection));
    }

    public int getTotalDistance() {
        return sections.stream().mapToInt(section -> section.getDistance()).sum();
    }
}


