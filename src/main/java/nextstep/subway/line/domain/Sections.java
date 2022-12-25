package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private void checkValidationForEmptySections() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public List<Section> getValues() {
        return this.sections;
    }

    public Optional<Section> getSectionUpLineStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();
    }

    public Optional<Section> getSectionDownLineStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(station))
                .findFirst();
    }

    public void addSection(Section section) {
        checkValidationForDuplicationSection(section.getUpStation(), section.getDownStation());
        checkValidationForValidSection(section.getUpStation(), section.getDownStation(), getStations());

        if (isEmptySection(section.getUpStation(), section.getDownStation(), section.getDistance())) {
            this.sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
            return;
        }

        updateSectionStation(section.getUpStation(), section.getDownStation(), section.getDistance());
        this.sections.add(section);
    }

    private void checkValidationForDuplicationSection(Station upStation, Station downStation) {
        if (isUpStationExisted(upStation) && isDownStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkValidationForValidSection(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
                stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it.equals(upStation));
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream().anyMatch(it -> it.equals(downStation));
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findSectionFirstStation();
        stations.add(downStation);

        settingDownStations(downStation, stations);

        return stations;
    }

    private boolean isEmptySection(Station upStation, Station downStation, int distance) {
        if (this.sections.isEmpty()) {
            return true;
        }

        return false;
    }

    private Station findSectionFirstStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (hasSectionNextUpStation(downStation)) {
            downStation = getSectionNextUpStation(downStation).get().getUpStation();
        }

        return downStation;
    }

    private boolean hasSectionNextUpStation(Station finalDownStation) {
        return getSectionNextUpStation(finalDownStation)
                .isPresent();
    }

    private Optional<Section> getSectionNextUpStation(Station finalDownStation) {
        return this.sections.stream()
                .filter(it -> it.getDownStation().equals(finalDownStation))
                .findFirst();
    }

    private void settingDownStations(Station downStation, List<Station> stations) {
        while (hasSectionNextDownStation(downStation)) {
            downStation = getSectionNextDownStation(downStation).get().getDownStation();
            stations.add(downStation);
        }
    }

    private boolean hasSectionNextDownStation(Station finalDownStation) {
        return getSectionNextDownStation(finalDownStation)
                .isPresent();
    }

    private Optional<Section> getSectionNextDownStation(Station finalDownStation) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
                .findFirst();
    }

    private void updateSectionStation(Station upStation, Station downStation, int distance) {
        if (isUpStationExisted(upStation)) {
            this.sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
        }else if (isDownStationExisted(downStation)) {
            this.sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }

    public void removeStation(Station station, Line line) {
        checkValidationForEmptySections();
        addIfRemovePositionMiddleSection(station, line);
        removeStation(getRemoveSections(station));
    }

    private void removeStation(List<Section> removeSections) {
        for (Section removeSection : removeSections) {
            this.sections.remove(removeSection);
        }
    }

    private void addIfRemovePositionMiddleSection(Station station, Line line) {
        Optional<Section> upLineStation = getSectionUpLineStation(station);
        Optional<Section> downLineStation = getSectionDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section newUpLineStation = upLineStation.get();
            Section newDownLineStation = downLineStation.get();
            int newDistance = newUpLineStation.getDistance() + newDownLineStation.getDistance();
            this.sections.add(new Section(line, newUpLineStation.getUpStation(), newDownLineStation.getDownStation(), newDistance));
        }
    }

    private List getRemoveSections(Station station) {
        return Arrays.asList(getSectionUpLineStation(station), getSectionDownLineStation(station))
                .stream()
                .filter(section -> section.isPresent())
                .map(section -> section.get())
                .collect(Collectors.toList());
    }
}
