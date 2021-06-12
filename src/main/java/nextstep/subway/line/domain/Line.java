package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;

        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.toCollection();
    }

    public List<Station> sortedStation2() {
        List<Station> results = new ArrayList<>();

        List<Section> copiedSections = new ArrayList<>(sections.toCollection());

        TopSection topSection = new TopSection(copiedSections);

        if (topSection.hasTopSection()) {
            results.add(topSection.getTopSection().getUpStation());
        }

        while (topSection.hasTopSection()) {
            Section section = topSection.getTopSection();
            results.add(section.getDownStation());
            copiedSections.remove(section);

            topSection = new TopSection(copiedSections);
        }

        return results;
    }

    public SortedStations sortedStation() {
        return sections.toSortedStations();
    }

    public void removeStation(Station station) {
        NewSection newSection = sections.removeStation(station);
        if (newSection != null) {
            Section section = newSection.toSection(this);
            sections.add(section);
        }
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = sortedStation2();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        Section section = new Section(this, upStation, downStation, distance);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            getSections().add(section);
        } else if (isDownStationExisted) {
            getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            getSections().add(section);
        } else {
            throw new RuntimeException();
        }
    }
}
