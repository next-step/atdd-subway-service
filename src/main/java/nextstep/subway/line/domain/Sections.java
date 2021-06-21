package nextstep.subway.line.domain;

import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final String NO_EXIST = "등록할 수 없는 구간 입니다.";
    private static final String ALREADY_EXIST = "이미 등록된 구간 입니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {}

    private Sections(Section sections) {
        this.sections.add(sections);
    }

    public static Sections of(Section section) {
        return new Sections(section);
    }

    public int getDistanceBetweenStations(Station upStation, Station downStation) {
        int distance = 0;
        Optional<Section> nextSection = findByUpstation(upStation);
        if (!nextSection.isPresent()) {
            return distance;
        }
        if (nextSection.get().getDownStation().equals(downStation)) {
            return nextSection.get().getDistance();
        }
        while (nextSection.isPresent() && !nextSection.get().getDownStation().equals(downStation)) {
            distance += nextSection.get().getDistance();
            nextSection = findNextSection(nextSection.get());
        }

        if (nextSection.isPresent()) {
            distance += nextSection.get().getDistance();
        }

        return distance;
    }

    public void removeStation(Station station, Line line) {
        validationRemove();

        Optional<Section> afterSection = findAfterSection(station);
        Optional<Section> beforeSection = findBeforeSection(station);

        if (afterSection.isPresent() && beforeSection.isPresent()) {
            Station newUpStation = beforeSection.get().getUpStation();
            Station newDownStation = afterSection.get().getDownStation();
            int newDistance = afterSection.get().getDistance() + beforeSection.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        afterSection.ifPresent(it -> this.sections.remove(it));
        beforeSection.ifPresent(it -> this.sections.remove(it));
    }

    public void addSection(Section section) {
        List<Station> statoins = getStations();
        validateAlreadyOrNoExist(section, statoins);
        updateIfUpStationMatch(section);
        updateIfDownStationMatch(section);
        sections.add(section);
    }

    public List<Station> getStations() {
        Optional<Section> nextSection = findStartSection();
        List<Station> stations = new ArrayList<>();

        if (sections.isEmpty() || !nextSection.isPresent()) {
            return Arrays.asList();
        }

        stations.add(nextSection.get().getUpStation());
        while (nextSection.isPresent()) {
            stations.add(nextSection.get().getDownStation());
            nextSection = findNextSection(nextSection.get());
        }

        return Collections.unmodifiableList(stations);

    }

    private Optional<Section> findNextSection(Section beforSection) {
        return sections.stream().filter(section -> section.isUpStationWithDown(beforSection))
            .findFirst();
    }

    private Optional<Section> findStartSection() {
        return sections.stream()
            .filter(section -> !findSectionIsAnotherDownStation(section).isPresent())
            .findFirst();
    }

    private Optional<Section> findSectionIsAnotherDownStation(Section beforeSection) {
        return sections.stream()
            .filter(section -> section.isDownStationWithUp(beforeSection))
            .findFirst();
    }

    private void updateIfDownStationMatch(Section compareSection) {
        sections.stream()
            .filter(compareSection::sameDownStation)
            .findFirst()
            .ifPresent(findedSection -> findedSection.updateDownStation(compareSection.getUpStation(),
                compareSection.getDistance()));
    }

    private void updateIfUpStationMatch(Section compareSection) {
        sections.stream()
            .filter(compareSection::sameUpStation)
            .findFirst()
            .ifPresent(findedSection -> findedSection.updateUpStation(compareSection.getDownStation(),
                compareSection.getDistance()));
    }

    private void validateAlreadyOrNoExist(Section compareSection, List<Station> statoins) {
        boolean hasUpStation = statoins.stream().anyMatch(station -> station.equals(compareSection.getUpStation()));
        boolean hasDownStation = statoins.stream().anyMatch(station -> station.equals(compareSection.getDownStation()));

        if (!hasUpStation && !hasDownStation) {
            throw new NoSuchElementException(NO_EXIST);
        }
        if (hasUpStation && hasDownStation) {
            throw new NoSuchElementException(ALREADY_EXIST);
        }
    }

    private Optional<Section> findBeforeSection(Station station) {
        return this.sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    private Optional<Section> findAfterSection(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private void validationRemove() {
        if (this.sections.size() <= 1) {
            throw new ProviderNotFoundException();
        }
    }

    private Optional<Section> findByUpstation(Station upStation) {
        return sections.stream()
            .filter(section -> section.sameUpStation(upStation))
            .findFirst();
    }

}
