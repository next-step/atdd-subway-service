package nextstep.subway.line.domain;

import nextstep.subway.common.exception.*;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
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

    private Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(final List<Section> sections) {
        return new Sections(sections);
    }

    public static Sections from(final Section section) {
        return new Sections(Collections.singletonList(section));
    }

    public static Sections empty() {
        return Sections.from(new ArrayList<>());
    }

    public void add(final Section targetSection, final Line line) {
        validateStationsContains(targetSection, line);
        targetSection.addLine(line);

        for (Section original : sections) {
            if (addUpStation(targetSection, original)) return;
            if (addDownStation(targetSection, original)) return;
            if (addUpStationAndMiddlStation(targetSection, original)) return;
            if (addDownStationAndMiddleStation(targetSection, original)) return;
        }
    }

    private boolean addUpStationAndMiddlStation(final Section targetSection, final Section original) {
        if (original.isUpStationAndTargetDownStationEquals(targetSection)) {
            addSectionOriginalIndex(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addDownStation(final Section targetSection, final Section original) {
        if (original.isDownStationEquals(targetSection)) {
            original.minusDistance(targetSection);
            original.changeDownStation(targetSection);
            addSectionBehindOfOriginal(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addDownStationAndMiddleStation(final Section targetSection, final Section original) {
        if (original.isDownStationAndTargetUpStationEquals(targetSection)) {
            addSectionBehindOfOriginal(targetSection, original);
            return true;
        }
        return false;
    }

    private boolean addUpStation(final Section targetSection, final Section original) {
        if (original.isUpStationEquals(targetSection)) {
            addSectionOriginalIndex(targetSection, original);
            original.minusDistance(targetSection);
            original.changeUpStation(targetSection);
            return true;
        }
        return false;
    }

    private void addSectionBehindOfOriginal(final Section targetSection, final Section original) {
        sections.add(sections.indexOf(original) + 1, targetSection);
    }

    private void addSectionOriginalIndex(final Section targetSection, final Section original) {
        sections.add(sections.indexOf(original), targetSection);
    }

    private void validateStationsContains(final Section section, final Line line) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        if (line.isContainStation(upStation) && line.isContainStation(downStation)) {
            throw new RegisterAllIncludeException();
        }
        if (!line.isContainStation(upStation) && !line.isContainStation(downStation)) {
            throw new RegisterNotAllIncludeException();
        }
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public boolean contains(final Station station) {
        return getStations().contains(station);
    }

    public List<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void merge(final Station station) {
        validateDeleteSection();

        final Section downSection = getDownSection(station);
        final Section upSection = getUpSection(station);
        downSection.merge(upSection);
        this.sections.remove(upSection);
    }

    private Section getDownSection(final Station station) {
        return sections.stream()
                .filter(section -> section.isDownStationEquals(station))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    private Section getUpSection(final Station station) {
        return sections.stream()
                .filter(section -> section.isUpStationEquals(station))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    private void validateDeleteSection() {
        if (isOneSection()) {
            throw new OneSectionDeleteException();
        }
        if (isNoSection()) {
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
