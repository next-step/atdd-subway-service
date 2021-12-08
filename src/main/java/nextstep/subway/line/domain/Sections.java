package nextstep.subway.line.domain;

import nextstep.subway.common.exception.*;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    private static final int INT_ONE = 1;
    private static final int INT_ZERO = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections;

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sectionList) {
        return new Sections(sectionList);
    }

    public static Sections from(Section section) {
        return new Sections(Arrays.asList(section));
    }

    public static Sections empty() {
        return Sections.from(new ArrayList<>());
    }

    public void add(Section targetSection, Line line) {
        validateStationsContains(targetSection, line);
        targetSection.addLine(line);

        for (Section original : sections) {
            if (addUpStation(targetSection, original)) return;
            if (addDownStation(targetSection, original)) return;
            if (addUpStationAndMiddlStation(targetSection, original)) return;
            if (addDownStationAndMiddleStation(targetSection, original)) return;
        }
    }

    private boolean addUpStationAndMiddlStation(Section targetSection, Section original) {
        if (original.isUpStationAndTargetDownStationEquals(targetSection)) {
            addSectionOriginalIndex(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addDownStation(Section targetSection, Section original) {
        if (original.isDownStationEquals(targetSection)) {
            original.minusDistance(targetSection);
            original.changeDownStation(targetSection);
            addSectionBehindOfOriginal(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addDownStationAndMiddleStation(Section targetSection, Section original) {
        if (original.isDownStationAndTargetUpStationEquals(targetSection)) {
            addSectionBehindOfOriginal(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addUpStation(Section targetSection, Section original) {
        if(original.isUpStationEquals(targetSection)) {
            addSectionOriginalIndex(targetSection, original);
            original.minusDistance(targetSection);
            original.changeUpStation(targetSection);
            return true;
        }
        return false;
    }

    private void addSectionBehindOfOriginal(Section targetSection, Section original) {
        sections.add(sections.indexOf(original) + 1, targetSection);
    }

    private void addSectionOriginalIndex(Section targetSection, Section original) {
        sections.add(sections.indexOf(original), targetSection);
    }

    private void validateStationsContains(Section section, Line line) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        if (line.isContainStation(upStation) && line.isContainStation(downStation)) {
            throw new RegisterAllIncludeException();
        }
        if (line.isContainStation(upStation) == false && line.isContainStation(downStation) == false) {
            throw new RegisterNotAllIncludeException();
        }
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public boolean contains(Station station) {
        return getStations().contains(station);
    }

    public List<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void merge(Station station) {
        validateDeleteSection();

        final Section downSection = getDownSection(station);
        final Section upSection = getUpSection(station);
        downSection.merge(upSection);
        this.sections.remove(upSection);
    }

    private Section getDownSection(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStationEquals(station))
                .findFirst()
                .orElseThrow(() -> new NotFoundException());
    }

    private Section getUpSection(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStationEquals(station))
                .findFirst()
                .orElseThrow(() -> new NotFoundException());
    }

    private void validateDeleteSection() {
        if(isOneSection()) {
            throw new OneSectionDeleteException();
        }
        if(isNoSection()) {
            throw new NoSectionDeleteException();
        }
    }

    private boolean isOneSection() {
        return this.sections.size() == INT_ONE;
    }

    private boolean isNoSection() {
        return this.sections.size() == INT_ZERO;
    }

}
