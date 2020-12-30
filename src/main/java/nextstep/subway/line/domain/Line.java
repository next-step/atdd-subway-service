package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.exceptions.InvalidAddSectionException;
import nextstep.subway.line.domain.exceptions.InvalidRemoveSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Transient
    public static final int MIN_SECTIONS_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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
        return sections;
    }

    public List<Station> getStations() {
        LineSectionExplorer lineSectionExplorer = new LineSectionExplorer(sections);
        return lineSectionExplorer.getStations();
    }

    public boolean addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();

        validateAddSection(stations, upStation, downStation);

        if (stations.isEmpty()) {
            return this.simpleAddSection(upStation, downStation, distance);
        }

        if (isUpStationExisted(stations, upStation)) {
            return addSectionWithSameUp(upStation, downStation, distance);
        }

        if (isDownStationExisted(stations, downStation)) {
            return addSectionWithSameDown(upStation, downStation, distance);
        }

        throw new InvalidAddSectionException("해당 구간을 추가할 수 없습니다.");
    }

    public boolean removeSection(Station station) {
        int originalSize = sections.size();

        if (originalSize <= MIN_SECTIONS_SIZE) {
            throw new InvalidRemoveSectionException("구간이 하나밖에 없는 지하철 노선의 구간을 제거할 수 없습니다.");
        }

        Optional<Section> upSection = sections.stream()
                .filter(it -> it.isSameWithUpStation(station))
                .findFirst();
        Optional<Section> downSection = sections.stream()
                .filter(it -> it.isSameWithDownStation(station))
                .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            sections.add(Section.mergeByTwoSections(upSection.get(), downSection.get()));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));

        return sections.size() == originalSize - 1;
    }

    boolean isSameName(Line thatLine) {
        return this.name.equals(thatLine.name);
    }

    private void validateAddSection(List<Station> stations, Station upStation, Station downStation) {
        validateIsAlreadyExist(stations, upStation, downStation);
        validateIsNotMatchAny(stations, upStation, downStation);
    }

    private void validateIsAlreadyExist(List<Station> stations, Station upStation, Station downStation) {
        if (isUpStationExisted(stations, upStation) && isDownStationExisted(stations, downStation)) {
            throw new InvalidAddSectionException("이미 등록된 구간 입니다.");
        }
    }

    private void validateIsNotMatchAny(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new InvalidAddSectionException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isUpStationExisted(List<Station> stations, Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private boolean isDownStationExisted(List<Station> stations, Station downStation) {
        return stations.stream().anyMatch(it -> it == downStation);
    }

    private boolean simpleAddSection(Station upStation, Station downStation, int distance) {
        return this.sections.add(new Section(this, upStation, downStation, distance));
    }

    private boolean addSectionWithSameUp(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.isSameWithUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        return simpleAddSection(upStation, downStation, distance);
    }

    private boolean addSectionWithSameDown(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.isSameWithDownStation(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        return simpleAddSection(upStation, downStation, distance);
    }
}
