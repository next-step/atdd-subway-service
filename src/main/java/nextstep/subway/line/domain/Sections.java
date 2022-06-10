package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    protected Sections() {
    }

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        if (Objects.isNull(sections)) {
            throw new IllegalArgumentException("null 을 입력할수 없습니다.");
        }
        this.sections.addAll(sections);
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return insertStationBySorted();
    }

    public int isSize() {
        return this.sections.size();
    }

    public Optional<Station> getStartStation() {
        if (sections.isEmpty()) {
            return Optional.empty();
        }
        return sections.stream().filter(this::isPreSection)
                .map(Section::getUpStation).findAny();
    }

    private List<Station> insertStationBySorted() {
        List<Station> result = new ArrayList<>();
        Optional<Station> isStartStation = getStartStation();
        while (isStartStation.isPresent()) {
            Station station = isStartStation.get();
            result.add(station);
            isStartStation = findNextStation(station);
        }
        return result;
    }

    private Optional<Station> findNextStation(final Station station) {
        return sections.stream()
                .filter(section -> section.isMatchUpStation(station))
                .map(Section::getDownStation)
                .findAny();
    }

    private boolean isPreSection(final Section source) {
        Optional<Section> isPreSection = sections.stream()
                .filter(section -> !section.isMatchDownStation(source.getUpStation()) &&
                        section.isMatchUpStation(source.getDownStation())).findAny();
        return isPreSection.isPresent();
    }
}
