package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        this.sections = sections;
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

    public void addSection(Section newSection) {
        boolean isUpStationExisted = sections.stream()
                .anyMatch(section -> section.hasStation(newSection.getUpStation()));
        boolean isDownStationExisted = sections.stream()
                .anyMatch(section -> section.hasStation(newSection.getDownStation()));

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

            sections.add(newSection);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == newSection.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

            sections.add(newSection);
        } else {
            throw new RuntimeException();
        }
    }

    public void removeSection(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> previousSection = sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
        Optional<Section> nextSection = sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();

        if (previousSection.isPresent() && nextSection.isPresent()) {
            Station newDownStation = nextSection.get().getDownStation();
            int newDistance = previousSection.get().getDistance() + nextSection.get().getDistance();
            previousSection.get().updateDownStation(newDownStation, newDistance);
            sections.remove(nextSection.get());
            return;
        }

        previousSection.ifPresent(it -> sections.remove(it));
        nextSection.ifPresent(it -> sections.remove(it));
    }

    public Station findUpStation() {
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
