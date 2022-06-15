package nextstep.subway.line.domain;

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

    public void add(Section section) {
        sections.add(section);
    }

    public void updateSection(Section requestSection) {
        Station upStation = requestSection.getUpStation();
        Station downStation = requestSection.getDownStation();
        List<Station> stations = orderedStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(downStation));

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
            stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(requestSection);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                .filter(it -> it.getUpStation().equals(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, requestSection.getDistance()));

            sections.add(requestSection);
        } else if (isDownStationExisted) {
            sections.stream()
                .filter(it -> it.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, requestSection.getDistance()));

            sections.add(requestSection);
        } else {
            throw new RuntimeException();
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

}
