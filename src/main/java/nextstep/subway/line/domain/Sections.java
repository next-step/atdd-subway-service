package nextstep.subway.line.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.util.CommonConstant;
import nextstep.subway.util.Message;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Embeddable
@NoArgsConstructor
public class Sections {
    @OneToMany(mappedBy = "line" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Builder
    public Sections(Line line, Station upStation, Station downStation, int distance) {
        sections.add(Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build());
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Section upEndSection = findUpEndSection();
        stations.add(upEndSection.getUpStation());
        Section nextSection = upEndSection;
        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findSectionByNextUpStation(nextSection.getDownStation());
        }
        return stations;
    }

    private Section findUpEndSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(toList());
        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst()
                .orElse(null);
    }

    public void add(Section section) {
        validateNotExistStation(section);
        alreadyExistStation(section);

        addUpStation(section);
        addDownStation(section);

        this.sections.add(section);
    }

    private void addDownStation(Section section) {
        sections.stream()
                .filter(oldSection -> section.getDownStation() == oldSection.getDownStation())
                .findFirst()
                .ifPresent(oldSection -> {
                    selectWithStation(section, oldSection, oldSection.getUpStation(), section.getUpStation());
                });
    }

    private void addUpStation(Section section) {
        sections.stream()
                .filter(oldSection -> section.getUpStation() == oldSection.getUpStation())
                .findFirst()
                .ifPresent(oldSection -> {
                    selectWithStation(section, oldSection, section.getDownStation(), oldSection.getDownStation());
                });
    }

    private void selectWithStation(Section section, Section oldSection, Station upStation, Station upStation2) {
        if (oldSection.getDistance() <= section.getDistance()) {
            throw new IllegalArgumentException(Message.DISTANCE_EXCESS_MESSAGE);
        }
        sections.add(new Section(oldSection.getLine(), upStation, upStation2, oldSection.getDistance() - section.getDistance()));
        sections.remove(oldSection);
    }

    private void validateNotExistStation(Section section) {
        if(!getStations().contains(section.getUpStation()) && !getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException(Message.NOT_EXIST_STATION_MESSAGE);
        }
    }

    private void alreadyExistStation(Section section) {
        if(getStations().containsAll(Arrays.asList(section.getUpStation(), section.getDownStation()))) {
            throw new IllegalArgumentException(Message.ALREADY_EXIST_STATION_MESSAGE);
        }
    }

    public void removeSection(Line line, Station station) {
        validateRemoveSection();

        Optional<Section> upSection = findByUpStation(station);
        Optional<Section> downSection = findByDownStation(station);
        validateRemoveStation(upSection.isPresent(), downSection.isPresent());

        if(upSection.isPresent() && downSection.isPresent()) {
            removeStation(line, upSection.get(), downSection.get());
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    private void removeStation(Line line, Section upSection, Section downSection) {
        Station upStation = downSection.getUpStation();
        Station downStation = upSection.getDownStation();
        int distance = upSection.getDistance() + downSection.getDistance();
        sections.add(Section.builder().line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build());
    }

    private void validateRemoveSection() {
        if(sections.size() <= CommonConstant.MIN_SECTION_SIZE) {
            throw new IllegalArgumentException(Message.ONE_SECTION_CANNOT_BE_UNDESSED);
        }
    }

    private Optional<Section> findByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isMatchUpStation(station))
                .findFirst();
    }

    private Optional<Section> findByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isMatchDownStation(station))
                .findFirst();
    }

    private void validateRemoveStation(boolean upStation, boolean downStation) {
        if (!upStation && !downStation) {
            throw new IllegalArgumentException(Message.NOT_REGISTER_STATION_CANNOT_REMOVE);
        }
    }
}
