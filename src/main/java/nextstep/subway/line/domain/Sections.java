package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        Section newSection = new Section(section);

        if (this.sections.isEmpty()) {
            return this.sections.add(newSection);
        }

        validateAdd(section);

        updateSectionForMatchStation(section);

        return this.sections.add(newSection);
    }

    public void add(Sections addingSections) {
        for (Section section : addingSections.sections) {
            this.sections.add(section);
        }
    }

    public boolean isEmpty()  {
        return this.sections.isEmpty();
    }

    private void updateSectionForMatchStation(Section section) {
        updateSectionForUpStaionMatch(section);
        updateSectionForDownStaionMatch(section);
    }

    private void updateSectionForDownStaionMatch(Section section) {
        this.findSectionByDownStation(section.getDownStation())
            .ifPresent(findedSection -> findedSection.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateSectionForUpStaionMatch(Section section) {
        this.findSectionByUpStation(section.getUpStation())
            .ifPresent(findedSection -> findedSection.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void validateAdd(Section section) {
        boolean isUpStationExisted = this.hasStation(section.getUpStation());
        boolean isDownStationExisted = this.hasStation(section.getDownStation());

        checkAllHasStation(isUpStationExisted, isDownStationExisted);
        checkAllNotHasStation(isUpStationExisted, isDownStationExisted);
    }

    private void checkAllNotHasStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkAllHasStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean hasStation(Station station) {
        return this.sections.stream()
                            .anyMatch(section -> section.hasStation(station));
    }

    public void deleteStation(Station station) {
        validateDelete();

        adjustSection(station);
    }

    private void adjustSection(Station station) {
        Optional<Section> upSection = this.findSectionByDownStation(station);
        Optional<Section> downSection = this.findSectionByUpStation(station);

        upSection.ifPresent(this.sections::remove);
        downSection.ifPresent(this.sections::remove);

        if (upSection.isPresent() && downSection.isPresent()) {
            this.sections.add(mergeSection(upSection.get(), downSection.get()));
        }
    }

    private Section mergeSection(Section upSection, Section downSection) {
        Station newUpStation = upSection.getUpStation();
        Station newDownStation = downSection.getDownStation();

        Distance newDistance = upSection.getDistance().plus(downSection.getDistance());

        return new Section(upSection.getLine(), newUpStation, newDownStation, newDistance);
    }

    private void validateDelete() {
        checkSectionCount();
    }

    private void checkSectionCount() {
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException("등록된 구간이 1개 이하여서 역을 삭제할 수 없습니다.");
        }
    }

    public List<Station> findStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();

        Station upStation = findUpTerminalStation();
        stations.add(upStation);

        Optional<Section> matchingStation = this.findSectionByUpStation(upStation);
        Station finalDownStation;

        while (matchingStation.isPresent()) {
            finalDownStation = matchingStation.get().getDownStation();
            matchingStation = this.findSectionByUpStation(finalDownStation);
            stations.add(finalDownStation);
        }

        return stations;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(this.sections);
    }

    private Station findUpTerminalStation() {
        Station upStation = this.sections.get(0).getUpStation();

        Optional<Section> firstSection = Optional.of(this.sections.get(0));

        while (firstSection.isPresent()) {
            upStation = firstSection.get().getUpStation();
            firstSection = this.findSectionByDownStation(upStation);
        }

        return upStation;
    }

    private Optional<Section> findSectionByUpStation(Station station){
        return this.sections.stream()
                            .filter(findedSection -> findedSection.isEqualUpStation(station))
                            .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station){
        return this.sections.stream()
                            .filter(findedSection -> findedSection.isEqualDownStation(station))
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
