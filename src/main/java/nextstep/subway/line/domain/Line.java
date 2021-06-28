package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    private int additionalCharge;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy("sequence ASC")
    private List<Section> sections = new LinkedList<>();

    public Line() {
    }

    public Line(String name, String color, int additionalCharge) {
        this.name = name;
        this.color = color;
        this.additionalCharge = additionalCharge;
    }

    public Line(String name, String color, int additionalCharge, Station upStation, Station downStation, int distance) {
        this(name, color, additionalCharge);
        Section e = new Section(this, upStation, downStation, distance, 0);
        sections.add(e);
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

    public int getAdditionalCharge() {
        return additionalCharge;
    }

    public void addSection(Section newSection) {
        Station upStation = newSection.getUpStation();
        Station downStation = newSection.getDownStation();
        int distance = newSection.getDistance();
        validateStationContains(newSection, this);

        changeSection(newSection, upStation, downStation, distance);
    }

    private void changeSection(Section newSection, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = this.sections.stream()
                .anyMatch(section -> section.isUpStationEquals(upStation));
        boolean isDownStationExisted = this.sections.stream()
                .anyMatch(section -> section.isDownStationEquals(downStation));
        boolean isUpStationAndDownStationNotEquals = this.sections.stream()
                .anyMatch(section -> section.isUpStationEquals(downStation));
        boolean isDownStationAndUpStationNotEquals = this.sections.stream()
                .anyMatch(section -> section.isDownStationEquals(upStation));
        whenEqualsUpStation(newSection, upStation, downStation, distance, isUpStationExisted, isDownStationExisted);
        whenEqualsDownStation(newSection, upStation, downStation, distance, isUpStationExisted, isDownStationExisted);
        whenNotEqualsUpStation(newSection, downStation, isUpStationAndDownStationNotEquals, isDownStationAndUpStationNotEquals, isUpStationExisted, isDownStationExisted);
        whenNotEqualsDownStation(newSection, upStation, isUpStationAndDownStationNotEquals, isDownStationAndUpStationNotEquals, isUpStationExisted, isDownStationExisted);
    }

    private void whenNotEqualsDownStation(Section newSection, Station upStation, boolean isUpStationAndDownStationNotEquals, boolean isDownStationAndUpStationNotEquals, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationAndDownStationNotEquals && isDownStationAndUpStationNotEquals && (!isDownStationExisted && !isUpStationExisted)) {
            updateIndexWhenNotEqualsDownStation(newSection, upStation);
        }
    }

    private void whenNotEqualsUpStation(Section newSection, Station downStation, boolean isUpStationAndDownStationNotEquals, boolean isDownStationAndUpStationNotEquals, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationAndDownStationNotEquals && !isDownStationAndUpStationNotEquals && (!isDownStationExisted && !isUpStationExisted)) {
            updateIndexWhenNotEqualsUpStation(newSection, downStation);
        }
    }

    private void whenEqualsDownStation(Section newSection, Station upStation, Station downStation, int distance, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && isDownStationExisted) {
            updateDownStation(newSection, upStation, downStation, distance);
        }
    }

    private void whenEqualsUpStation(Section newSection, Station upStation, Station downStation, int distance, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && !isDownStationExisted) {
            updateUpStation(newSection, upStation, downStation, distance);
        }
    }

    private void updateIndexWhenNotEqualsDownStation(Section newSection, Station upStation) {
        this.sections.stream()
                .filter(section -> section.isDownStationAndUpStationEquals(upStation))
                .findFirst()
                .ifPresent(section -> {
                            addSectionOriginalIndex(newSection, section);
                            doSyncSequence();
                        }
                );
    }

    private void updateIndexWhenNotEqualsUpStation(Section newSection, Station downStation) {
        this.sections.stream()
                .filter(section -> section.isUpStationAndDownStationEquals(downStation))
                .findFirst()
                .ifPresent(section -> {
                            addSectionBehindOfOriginal(newSection, section);
                            doSyncSequence();
                        }
                );

    }

    private void validateStationContains(Section newSection, Line line) {
        Station upStation = newSection.getUpStation();
        Station downStation = newSection.getDownStation();
        checkNotContainingStations(line, upStation, downStation);
        checkContainingStations(line, upStation, downStation);
    }

    private void checkContainingStations(Line line, Station upStation, Station downStation) {
        if (line.isContainingStation(upStation) &&
                line.isContainingStation(downStation)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean isContainingStation(Station station) {
        return getStations().contains(station);
    }

    private void checkNotContainingStations(Line line, Station upStation, Station downStation) {
        if (line.isContainingStation(upStation) == false &&
                line.isContainingStation(downStation) == false) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }


    private void doSyncSequence() {
        List<Section> list = this.sections;
        for (int i = 0; i < list.size(); i++) {
            Section section = list.get(i);
            section.setSequence(i);
        }
    }

    private void addSectionBehindOfOriginal(Section targetSection, Section original) {
        int index = this.sections.indexOf(original) + 1;
        this.sections.add(index, targetSection);
    }

    private void addSectionOriginalIndex(Section targetSection, Section original) {
        this.sections.add(this.sections.indexOf(original), targetSection);
    }

    private void updateDownStation(Section newSection, Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(section -> {
                    section.updateDownStation(upStation, distance);
                    this.sections.add(sections.indexOf(section) + 1, newSection);
                    doSyncSequence();
                });
    }

    private void updateUpStation(Section newSection, Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(section -> section.isUpStationEquals(upStation))
                .findFirst()
                .ifPresent(section -> {
                    section.updateUpStation(downStation, distance);
                    this.sections.add(sections.indexOf(section), newSection);
                    doSyncSequence();
                });

    }

    public List<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void removeStation(Station station) {
        AdjacentSections adjacentSections = getSectionsContainingStation(station);
        adjacentSections.merge(this.sections);
    }

    private AdjacentSections getSectionsContainingStation(Station station) {
        List<Section> collect = this.sections.stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .collect(Collectors.toList());

        return AdjacentSections.of(collect);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
