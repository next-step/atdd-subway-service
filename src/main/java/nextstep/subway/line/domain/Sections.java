package nextstep.subway.line.domain;

import nextstep.subway.common.exception.NoResultException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int REQUIRE_CONNECTED_STATION_COUNT = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(final Section... sections) {
        this.sections.addAll(Arrays.asList(sections));
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(final Section section) {
        this.validateAddSection(section);
        this.adjustDistance(section);
        this.sections.add(section);
    }

    private void validateAddSection(final Section addSection) {
        boolean noConnectable = this.sections
                .stream()
                .noneMatch(section -> section.connectable(addSection));

        if (noConnectable) {
            throw new IllegalArgumentException("연결할 수 없는 역이 포함되어 있습니다.");
        }
    }

    private void adjustDistance(final Section addSection) {
        this.sections.forEach(section -> section.subtractDistance(addSection));
    }

    public void deleteStation(final Station station) {
        List<Section> connectedSections = this.validateDeleteStation(station);
        Section deleteSection = this.peekSectionWithUpStation(connectedSections, station);
        Section section = this.peekSectionWithDownStation(connectedSections, station);
        section.merge(deleteSection);
        this.sections.remove(deleteSection);
    }

    private List<Section> validateDeleteStation(final Station deleteStation) {
        List<Section> connectedSections = this.sections
                .stream()
                .filter(section -> section.hasStation(deleteStation))
                .collect(Collectors.toList());

        if (connectedSections.size() != REQUIRE_CONNECTED_STATION_COUNT) {
            throw new IllegalArgumentException("삭제할 수 없는 역입니다.");
        }

        return connectedSections;
    }

    private Section peekSectionWithUpStation(final List<Section> connectedSections, final Station station) {
        return connectedSections
                .stream()
                .filter(section -> section.hasUpStation(station))
                .findFirst()
                .orElseThrow(() -> new NoResultException("찾을 수 없는 구간입니다."));
    }

    private Section peekSectionWithDownStation(final List<Section> connectedSections, final Station station) {
        return connectedSections
                .stream()
                .filter(section -> section.hasDownStation(station))
                .findFirst()
                .orElseThrow(() -> new NoResultException("찾을 수 없는 구간입니다."));
    }

}
