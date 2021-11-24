package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
        this.sections = new ArrayList<>();
    }

    private Sections(Section ... addingSections) {
        this.sections = new ArrayList<>();

        for(int index = 0; index < addingSections.length; index++) {
            this.sections.add(new Section(addingSections[index]));
        }
    }

    public static Sections of(Section ... addingSections) {
        return new Sections(addingSections);
    }

    public boolean add(Section section) {
        if (this.sections.isEmpty()) {
            return this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        }

        boolean isUpStationExisted = this.hasStation(section.getUpStation());
        boolean isDownStationExisted = this.hasStation(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }

        Section newSection = new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance());

        if (isUpStationExisted) {
            this.findByUpStation(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            return this.sections.add(newSection);
        }

        this.findByDownStation(section.getDownStation())
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        return this.sections.add(newSection);
    }

    private boolean hasStation(Station station) {
        return this.sections.stream()
                            .anyMatch(section -> section.getUpStation().equals(station)
                                                || section.getDownStation().equals(station));
    }

    public void deleteStation(Line line, Station station) {
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException("등록된 구간이 1개 이하여서 역을 삭제할 수 없습니다.");
        }

        Optional<Section> downSectionAtMatchingStation = this.findByUpStation(station);
        Optional<Section> upSectionAtMatchingStation = this.findByDownStation(station);

        if (downSectionAtMatchingStation.isPresent() && upSectionAtMatchingStation.isPresent()) {
            Station newUpStation = upSectionAtMatchingStation.get().getUpStation();
            Station newDownStation = downSectionAtMatchingStation.get().getDownStation();

            int newDistance = downSectionAtMatchingStation.get().getDistance() + upSectionAtMatchingStation.get().getDistance();

            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        downSectionAtMatchingStation.ifPresent(this.sections::remove);
        upSectionAtMatchingStation.ifPresent(this.sections::remove);
    }

    public List<Station> findStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();

        Station upStation = findUpTerminalStation();
        stations.add(upStation);

        Optional<Section> matchingStation = this.findByUpStation(upStation);
        Station finalDownStation;

        while (matchingStation.isPresent()) {
            finalDownStation = matchingStation.get().getDownStation();
            matchingStation = this.findByUpStation(finalDownStation);
            stations.add(finalDownStation);
        }

        return stations;
    }

    private Station findUpTerminalStation() {
        Station upStation = this.sections.get(0).getUpStation();

        Optional<Section> firstSection = Optional.of(this.sections.get(0));

        while (firstSection.isPresent()) {
            upStation = firstSection.get().getUpStation();
            firstSection = this.findByDownStation(upStation);
        }

        return upStation;
    }

    private Optional<Section> findByUpStation(Station station){
        return this.sections.stream()
                            .filter(it -> it.getUpStation().equals(station))
                            .findFirst();
    }

    private Optional<Section> findByDownStation(Station station){
        return  this.sections.stream()
                                .filter(it -> it.getDownStation().equals(station))
                                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Sections)) {
            return false;
        }
        Sections equalingSections = (Sections) o;
        return Objects.equals(sections, equalingSections.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sections);
    }
}
