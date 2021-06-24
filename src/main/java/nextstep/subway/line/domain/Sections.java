package nextstep.subway.line.domain;

import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.line.LineAlreadyExistException;
import nextstep.subway.exception.line.NotFoundSectionException;
import nextstep.subway.exception.line.NotFoundStationsException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

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
        boolean isFinish = false;
        List<Section> orderedSections = getSectionsWithOrder();
        for (Section sectoin : orderedSections) {
            distance = sumDistance(upStation, distance, isFinish, sectoin);
            isFinish = sectoin.sameDownStation(downStation);
        }

        return distance;
    }

    private int sumDistance(Station upStation, int distance, boolean isFinish, Section sectoin) {
        if (isFinish) {
            return distance;
        }

        if (sectoin.sameUpStation(upStation) || distance != 0) {
            distance += sectoin.getDistance();
        }
        return distance;
    }

    public void removeStation(Station station, Line line) {
        validationRemove();

        Optional<Section> nullAbleAfterSection = findAfterSection(station);
        Optional<Section> nullAbleBeforeSection = findBeforeSection(station);

        if (nullAbleAfterSection.isPresent() && nullAbleBeforeSection.isPresent()) {
            Section afterSectoin = nullAbleAfterSection.get();
            Section beforeSection = nullAbleBeforeSection.get();
            int newDistance = afterSectoin.getDistance() + beforeSection.getDistance();
            this.sections
                .add(new Section(line, beforeSection.getUpStation(), afterSectoin.getDownStation(), newDistance));
        }

        nullAbleAfterSection.ifPresent(it -> this.sections.remove(it));
        nullAbleBeforeSection.ifPresent(it -> this.sections.remove(it));
    }

    public void addSection(Section section) {
        List<Station> statoins = getStations();
        validateAlreadyOrNoExist(section, statoins);
        updateIfUpStationMatch(section);
        updateIfDownStationMatch(section);
        sections.add(section);
    }

    public List<Station> getStations() {
        List<Station> stations = getSectionsWithOrder().stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .distinct()
            .collect(Collectors.toList());

        return Collections.unmodifiableList(stations);

    }

    private List<Section> getSectionsWithOrder() {
        List<Section> sortedSections = new ArrayList<>();
        Section startSection = findStartSection();
        sortedSections.add(startSection);
        addSectionIfExist(sortedSections, findNextSection(startSection));
        return sortedSections;
    }

    private void addSectionIfExist(List<Section> sortedSections, Optional<Section> nextSection) {
        if (!nextSection.isPresent()) {
            return;
        }
        Section section = nextSection.get();
        sortedSections.add(section);
        addSectionIfExist(sortedSections, findNextSection(section));
    }

    private Optional<Section> findNextSection(Section beforSection) {
        return sections.stream().filter(section -> section.isUpStationWithDown(beforSection))
            .findFirst();
    }

    private Section findStartSection() {
        return sections.stream()
            .filter(section -> !findSectionIsAnotherDownStation(section).isPresent())
            .findFirst()
            .orElseThrow(NotFoundSectionException::new);

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
            throw new NotFoundStationsException();
        }
        if (hasUpStation && hasDownStation) {
            throw new LineAlreadyExistException();
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

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

}
