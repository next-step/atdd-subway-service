package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        sections.add(section);
    }

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        checkExistsSection(isUpStationExisted, isDownStationExisted);
        checkNotAddSection(newSection, stations);

        if (stations.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            addFrontSection(newSection);
            return;
        }

        if (isDownStationExisted) {
            addBackSection(newSection);
        }
    }

    private void addBackSection(Section newSection) {
        this.sections.stream()
                .filter(it -> it.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

        this.sections.add(newSection);
    }

    private void addFrontSection(Section newSection) {
        this.sections.stream()
                .filter(it -> it.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        this.sections.add(newSection);
    }

    private void checkNotAddSection(Section newSection, List<Station> stations) {
        if (!stations.isEmpty() && notExistsStation(newSection, stations)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean notExistsStation(Section newSection, List<Station> stations) {
        return stations.stream().noneMatch(it -> it == newSection.getUpStation()) && stations.stream().noneMatch(it -> it == newSection.getDownStation());
    }

    private void checkExistsSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
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

    public List<Section> getSections() {
        return sections;
    }

    public void removeSection(Line line, Station station) {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }
}
