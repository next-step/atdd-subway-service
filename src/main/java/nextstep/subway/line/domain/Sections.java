package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(
        mappedBy = "line",
        cascade = CascadeType.PERSIST,
        orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section){
        sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        validateAleadyExist(upStation, downStation);
        validateRegister(upStation, downStation);

        if (sections.isEmpty()) {
            add(Section.of(line, upStation, downStation, distance));
            return;
        }

        updateUpStation(upStation, downStation, distance);
        updateDownStation(upStation, downStation, distance);
        add(Section.of(line, upStation, downStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, Distance distance) {
        findUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, Distance distance){
        findDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean isDownStationExisted(Station downStation) {
        return this.sections.stream()
                .anyMatch(it -> it.isSameDownStation(downStation));
    }

    private boolean isUpStationExisted(Station upStation) {
        return this.sections.stream()
                .anyMatch(it -> it.isSameUpStation(upStation));
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void removeStation(Line line, Station station) {
        validateSize();

        Optional<Section> upSection = findUpStation(station);
        Optional<Section> downSection = findDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            Distance newDistance = Distance.from(upSection.get().getDistance(), downSection.get().getDistance());
            add(Section.of(line, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(this::removeSection);
        downSection.ifPresent(this::removeSection);
    }

    private Optional<Section> findDownStation(Station downStation){
        return this.sections.stream()
                .filter(it -> it.isSameDownStation(downStation))
                .findFirst();
    }

    private Optional<Section> findUpStation(Station upStation){
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(upStation))
                .findFirst();
    }

    private Station findTerminalUpStation() {
        return sections.stream()
                .filter(section -> sections.stream().map(Section::getDownStation).noneMatch(Predicate.isEqual(section.getUpStation())))
                .findFirst()
                .map(Section::getUpStation)
                .orElseThrow(RuntimeException::new);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findTerminalUpStation();
        stations.add(downStation);

        Section nextSection = findUpStation(downStation).orElse(null);
        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findUpStation(nextSection.getDownStation()).orElse(null);
        }

        return stations;
    }

    public int getSize(){
        return this.sections.size();
    }

    public List<Section> getSections() {
        return sections;
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

    private void validateSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
