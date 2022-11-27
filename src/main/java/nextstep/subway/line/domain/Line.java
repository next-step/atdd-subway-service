package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections;

    public Line() {
        this(null, null, null, null, 0);
    }

    public Line(String name, String color) {
        this(name, color, null, null, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(new Section(this, upStation, downStation, distance));
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

    public Set<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSections() {
        return sections.value();
    }

    public void addSection(final Section section) {
        validateSection(section);
        if (addInitialSection(section)) return;
        if (addUpSection(section)) return;
        addDownSection(section);
    }

    private void addDownSection(final Section section) {
        if (isStationExisted(section.downStation())) {
            Section originalSection = sections.findSameDownStation(section);
            if (Objects.nonNull(originalSection)) {
                originalSection.updateDownStation(section);
            }
            sections.add(section, this::syncLine);
        }
    }

    private boolean addUpSection(final Section section) {
        if (isStationExisted(section.upStation())) {
            Section originalSection = sections.findSameUpStation(section);
            if (Objects.nonNull(originalSection)) {
                originalSection.updateUpStation(section);
            }
            sections.add(section, this::syncLine);
            return true;
        }
        return false;
    }

    private boolean addInitialSection(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section, this::syncLine);
            return true;
        }
        return false;
    }

    private void validateSection(final Section section) {
        if (isStationExisted(section.upStation()) && isStationExisted(section.downStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!getStations().isEmpty() && noneMatchUpStation(section) && noneMatchDownStation(section)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean noneMatchDownStation(final Section section) {
        return getStations().stream().noneMatch(section::isSameDownStation);
    }

    private boolean noneMatchUpStation(final Section section) {
        return getStations().stream().noneMatch(section::isSameUpStation);
    }

    private boolean isStationExisted(final Station station) {
        return getStations().stream().anyMatch(it -> it.equals(station));
    }

    public void deleteStationById(final long stationId) {
        validateIsDeletableStation();
        sections.remove(stationId, this::syncLine);
    }

    private void syncLine(Section section) {
        section.setLine(this);
    }

    private void validateIsDeletableStation() {
        if (sections.count() <= 1) {
            throw new IllegalArgumentException("삭제할 수 있는 구간이 없습니다.");
        }
    }
}
