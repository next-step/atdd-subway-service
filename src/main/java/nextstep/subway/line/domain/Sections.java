package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections initSections() {
        return new Sections(new ArrayList<>());
    }

    public void add(Section section) {
        checkUniqueSection(section);
        checkValidSection(section);
        updateUpStation(section);
        updateDownStation(section);
        this.sections.add(section);
    }

    private void checkUniqueSection(Section section) {
        if (this.sections.contains(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkValidSection(Section section) {
        List<Station> stations = this.getStations();

        // noneMatch -> 모든 요소들이 주어진 조건을 만족하는지
        if (!stations.isEmpty() && stations.stream().noneMatch(st -> st == section.getUpStation()) &&
                stations.stream().noneMatch(st -> st == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(st -> st == newSection.getUpStation());

        // upStation 일치하면 downStation 변경
        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(se -> se.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(se -> se.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        }
    }

    private void updateDownStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isDownStationExisted = stations.stream().anyMatch(st -> st == newSection.getDownStation());

        // downStation 일치하면 upStation 변경
        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(se -> se.getDownStation() == newSection.getDownStation())
                    .findFirst()
                    .ifPresent(se-> se.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Section> sortedSections = sortSections();
        return sortedSections.stream()
                .flatMap(Section::stations)// Stream<Station> return
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Section> sortSections() {
        return sections.stream()
                .sorted(Section::compareTo)
                .collect(Collectors.toList());
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
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


    public List<Section> getSections() {
        return sections;
    }
}
