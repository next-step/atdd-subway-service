package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void add(Section newSection) {

        validateAlreadyUsedSection(newSection);
        validateNotUsedStations(newSection);

        if (this.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        if (this.anyMatch(newSection.getUpStation())) {
            this.updateUpStation(newSection);
            this.sections.add(newSection);
            return;
        }

        if (this.anyMatch(newSection.getDownStation())) {
            this.updateDownStation(newSection);
            this.sections.add(newSection);
            return;
        }

        throw new RuntimeException();


    }


    private void validateNotUsedStations(Section section) {
        if (!this.isEmpty() && this.noneMatch(section.getUpStation()) &&
            this.noneMatch(section.getDownStation())) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateAlreadyUsedSection(Section section) {

        if (this.anyMatch(section.getUpStation()) && this.anyMatch(section.getDownStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }


    }

    public boolean noneMatch(Station station) {
        return Stream
            .concat(sections.stream().map(Section::getUpStation), sections.stream().map(Section::getDownStation))
            .noneMatch(it -> it == station);
    }

    public boolean anyMatch(Station station) {
        return Stream
            .concat(sections.stream().map(Section::getUpStation), sections.stream().map(Section::getDownStation))
            .anyMatch(it -> it == station);
    }


    private boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public Stations getStations() {
        if (this.sections.isEmpty()) {
            return new Stations(Collections.emptyList());
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return new Stations(stations);
    }

    private Station findFirstStation() {
        if (this.sections.isEmpty()) {
            throw new IllegalArgumentException("존재하는 Section이 없습니다.");
        }
        Station firstStation = this.getSections().get(0).getUpStation();
        while (firstStation != null) {
            Station downStation = firstStation;
            Optional<Section> beforeSection = findDownStation(downStation);
            if (!beforeSection.isPresent()) {
                break;
            }
            firstStation = beforeSection.get().getUpStation();
        }
        return firstStation;
    }


    public void updateUpStation(Section section) {
        findUpStation(section.getUpStation())
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStation(Section section) {
        findDownStation(section.getDownStation())
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public Optional<Section> findUpStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    public Optional<Section> findDownStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    public int size() {
        return this.sections.size();
    }

    public void remove(Section section) {
        this.sections.remove(section);
    }
}
