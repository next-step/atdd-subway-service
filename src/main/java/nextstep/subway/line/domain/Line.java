package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        this.sections = new Sections();
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
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

    public List<Section> getSections() {
        return sections.value();
    }

    public Set<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(final Section section) {
        validateSection(section);
        if (addInitialSection(section)) return;
        if (addUpSection(section)) return;
        addDownSection(section);
    }

    private void addDownSection(final Section section) {
        if (isStationExisted(section.getDownStation())) {
            Optional<Section> originalSection = sections.findSameDownStation(section);
            originalSection.ifPresent(it -> it.updateDownStation(section));
            syncLine(section);
            sections.add(section, this::syncLine);
        }
    }

    private boolean addUpSection(final Section section) {
        if (isStationExisted(section.getUpStation())) {
            Optional<Section> originalSection = sections.findSameUpStation(section);
            originalSection.ifPresent(it -> it.updateUpStation(section));
            syncLine(section);
            sections.add(section, this::syncLine);
            return true;
        }
        return false;
    }

    private boolean addInitialSection(final Section section) {
        if (sections.count() == 0) {
            syncLine(section);
            sections.add(section, this::syncLine);
            return true;
        }
        return false;
    }

    private void validateSection(final Section section) {
        if (isStationExisted(section.getUpStation()) && isStationExisted(section.getDownStation())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!getStations().isEmpty()
                && getStations().stream().noneMatch(it -> it.equals(section.getUpStation()))
                && getStations().stream().noneMatch(it -> it.equals(section.getDownStation()))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
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
            throw new RuntimeException();
        }
    }
}
