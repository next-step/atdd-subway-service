package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> section.isEqualsUpStation(it));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> section.isEqualsDownStation(it));

        validateAlreadyRegisteredSection(isUpStationExisted, isDownStationExisted);
        validateNotFound(section, stations);

        if (stations.isEmpty()) {
            this.sections.add(section);
            return;
        }

        addSection(section, isUpStationExisted, isDownStationExisted);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        stations.add(upStation);
        addStations(stations, upStation);

        return stations;
    }

    public void remove(Line line, Station station) {
        validateOnlyOneSection();

        Optional<Section> downLineStation = findSection(it -> it.isEqualsDownStation(station));
        Optional<Section> upLineStation = findSection(it -> it.isEqualsUpStation(station));

        createNewSectionByRemove(line, downLineStation, upLineStation);
        remove(downLineStation, upLineStation);
    }

    private Station findAnyDownStation() {
        return this.sections.get(0).getDownStation();
    }

    private Optional<Section> findSection(Predicate<Section> sectionPredicate) {
        return this.sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    private void remove(Optional<Section> downLineStation, Optional<Section> upLineStation) {
        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private void createNewSectionByRemove(Line line, Optional<Section> downLineStation, Optional<Section> upLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = downLineStation.get();
            Section downSection = upLineStation.get();

            Station newUpStation = upSection.getUpStation();
            Station newDownStation = downSection.getDownStation();
            Distance newDistance = downSection.plusDistance(upSection);
            this.sections.add(Section.of(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void addSection(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted) {
            updateSection(it -> it.isEqualsUpStation(section),
                    it -> it.updateUpStation(section));
            this.sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            updateSection(it -> it.isEqualsDownStation(section),
                    it -> it.updateDownStation(section));
            this.sections.add(section);
            return;
        }

        throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
    }

    private void updateSection(Predicate<Section> sectionPredicate, Consumer<Section> sectionConsumer) {
        this.sections.stream()
                .filter(sectionPredicate)
                .findFirst()
                .ifPresent(sectionConsumer);
    }

    private void validateNotFound(Section section, List<Station> stations) {
        if (!stations.isEmpty() &&
                stations.stream()
                        .noneMatch(it -> section.isEqualsUpStation(it)) &&
                stations.stream()
                        .noneMatch(it -> section.isEqualsDownStation(it))) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateAlreadyRegisteredSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void addStations(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = findSection(it -> it.isEqualsUpStation(finalDownStation));

            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }
    }

    private Station findUpStation() {
        Station downStation = findAnyDownStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSection(it -> it.isEqualsDownStation(finalDownStation));

            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    private void validateOnlyOneSection() {
        if (this.sections.size() == 1) {
            throw new IllegalArgumentException("구간이 1개일 경우에는 역을 제거할 수 없습니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
