package nextstep.subway.sections.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        this.sections.add(section);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void updateSection(Section requestSection) {
        if (orderedStations().isEmpty()) {
            sections.add(requestSection);
            return;
        }

        Station sameUpStation = findSameStation(requestSection.getUpStation());
        Station sameDownStation = findSameStation(requestSection.getDownStation());

        validate(sameUpStation, sameDownStation);

        updateSection(requestSection, sameUpStation, sameDownStation);
    }

    private void updateSection(Section requestSection, Station sameUpStation,
        Station sameDownStation) {
        if (sameUpStation != null) {
            updateStation(requestSection.getDownStation(), sameUpStation,
                requestSection.getDistance());
            sections.add(requestSection);
        }

        if (sameDownStation != null) {
            updateStation(requestSection.getUpStation(), sameUpStation,
                requestSection.getDistance());
            sections.add(requestSection);
        }
    }

    private void updateStation(Station station, Station sameUpStation, int distance) {
        sections.stream()
            .filter(it -> it.getUpStation().equals(sameUpStation))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(station, distance));
    }

    private void validate(Station sameUpStation, Station sameDownStation) {
        if (sameUpStation != null && sameDownStation != null) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (sameUpStation == null && sameDownStation == null) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> orderedStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
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
                .filter(it -> it.getDownStation().equals(finalDownStation))
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void delete(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst();
        Optional<Section> downLineStation = sections.stream()
            .filter(it -> it.getDownStation().equals(station))
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance =
                upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation,
                newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Station findSameStation(Station station) {

        return orderedStations().stream().filter(it -> it.equals(station)).findFirst().orElse(null);
    }


}
