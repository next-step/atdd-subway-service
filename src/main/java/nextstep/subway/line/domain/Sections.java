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
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section){
        sections.add(section);
    }

    public void addSections(Line line, Station upStation, Station downStation, int distance) {
        validateAleadyExist(upStation, downStation);
        validateRegister(upStation, downStation);

        if (getStations().isEmpty()) {
            add(new Section(line, upStation, downStation, distance));
        }

        if (isUpStationExisted(upStation)) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted(downStation)) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            add(new Section(line, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream().anyMatch(it -> it == downStation);
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    private void validateAleadyExist(Station upStation, Station downStation){
        if (isUpStationExisted(upStation) && isDownStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateRegister(Station upStation, Station downStation) {
        List<Station> stations = getStations();
        if (
            !stations.isEmpty()
            && stations.stream().noneMatch(it -> it == upStation)
            && stations.stream().noneMatch(it -> it == downStation)
        ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void removeStation(Line line, Station station) {
        validateEmpty();

        Optional<Section> upLineStation = getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(this::removeSection);
        downLineStation.ifPresent(this::removeSection);
    }

    private void validateEmpty() {
        if (getStations().size() <= 1) {
            throw new RuntimeException();
        }
    }

    public List<Section> getSections() {
        return sections;
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
            Optional<Section> nextLineStation = this.sections.stream()
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
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
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
