package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
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

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
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

    public void removeStation(Station 지울_정거장) {
        Optional<Section> downSectionOptional = sections.stream().filter(
            section -> section.getUpStation().equals(지울_정거장)
        ).findFirst();

        Optional<Section> upSectionOptional = sections.stream().filter(
            section -> section.getDownStation().equals(지울_정거장)
        ).findFirst();

        validateOnlyOneSection();
        validateNoSectionForRemove(downSectionOptional, upSectionOptional);

        Section downSection = downSectionOptional.orElse(Section.emptyOf(this));
        Section upSection = upSectionOptional.orElse(Section.emptyOf(this));

        Section mergeSection = Section.mergeOf(downSection, upSection);

        sections.add(mergeSection);
        sections.remove(downSection);
        sections.remove(upSection);
    }

    private void validateNoSectionForRemove(Optional<Section> downSection,
        Optional<Section> upSection) {
        if (!downSection.isPresent() && !upSection.isPresent()) {
            throw new RuntimeException();
        }
    }

    private void validateOnlyOneSection() {
        if (sections.size() == 1) {
            throw new RuntimeException();
        }
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        Section appendSection = new Section(this, upStation, downStation, distance);
        if (stations.isEmpty()) {
            sections.add(appendSection);
            return;
        }

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validateDuplicateSection(isUpStationExisted, isDownStationExisted);
        validateNoBaseStation(stations, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            insertSection(appendSection, upStation, true);
            return;
        }
        if (isDownStationExisted) {
            insertSection(appendSection, downStation, false);
            return;
        }

    }

    private void insertSection(Section appendSection, Station baseStation, boolean baseIsUp) {
        Section baseSection = getBaseSection(baseStation, baseIsUp)
            .orElseThrow(RuntimeException::new);

        baseSection.add(appendSection, baseIsUp);

        sections.add(appendSection);
    }

    private Optional<Section> getBaseSection(Station baseStation, boolean baseIsUp) {
        for (Section section : sections) {
            if (baseIsUp) {
                if (section.getUpStation().equals(baseStation)) {
                    return Optional.of(section);
                }
                if (section.getDownStation().equals(baseStation)) {
                    return Optional.of(section);
                }
            }

            if (!baseIsUp) {
                if (section.getDownStation().equals(baseStation)) {
                    return Optional.of(section);
                }
                if (section.getUpStation().equals(baseStation)) {
                    return Optional.of(section);
                }
            }
        }
        return Optional.empty();
    }

    private void validateNoBaseStation(List<Station> stations, boolean isUpStationExisted,
        boolean isDownStationExisted) {
        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateDuplicateSection(boolean isUpStationExisted,
        boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
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


}
