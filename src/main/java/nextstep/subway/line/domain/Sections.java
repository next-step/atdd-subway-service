package nextstep.subway.line.domain;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public static Sections empty() {
        return new Sections(new ArrayList<>());
    }

    public void connect(Section section) {
        Section upBoundSection = upBoundSection(section);
        Section downBoundSection = downBoundSection(section);

        validateForConnect(section);

        updateWhenConnectInMiddleOfSection(upBoundSection, downBoundSection, section);
        this.sections.add(section);
    }

    private Section upBoundSection(Section standardSection) {
        return this.sections.stream()
            .filter(section -> section.isEqualToUpStation(standardSection.getUpStation()))
            .findFirst()
            .orElse(Section.DUMMY_SECTION);
    }

    private Section downBoundSection(Section standardSection) {
        return this.sections.stream()
            .filter(section -> section.isEqualToDownStation(standardSection.getDownStation()))
            .findFirst()
            .orElse(Section.DUMMY_SECTION);
    }

    public List<Station> getStations() {
        if (CollectionUtils.isEmpty(sections)) {
            return Lists.newArrayList();
        }

        return Stream.of(getUpStations(), getDownStations())
            .flatMap(Collection::stream)
            .distinct()
            .collect(toList());
    }

    private Set<Station> getUpStations() {
        return this.sections.stream()
            .map(Section::getUpStation)
            .collect(toSet());
    }

    private Set<Station> getDownStations() {
        return this.sections.stream()
            .map(Section::getDownStation)
            .collect(toSet());
    }

    private void updateWhenConnectInMiddleOfSection(Section upBoundSection,
        Section downBoundSection,
        Section section) {
        List<Station> stations = getStations();
        if (connectableByUpStation(upBoundSection, stations.contains(section.getUpStation()))) {
            upBoundSection.connectByUpStation(section);
            return;
        }

        if (connectableByDownStation(downBoundSection, stations.contains(section.getDownStation()))) {
            downBoundSection.connectByDownStation(section);
        }
    }

    private boolean connectableByUpStation(Section upBoundSection, boolean contains) {
        return !upBoundSection.isDummy() && contains;
    }

    private boolean connectableByDownStation(Section downBoundSection, boolean contains) {
        return !downBoundSection.isDummy() && contains;
    }

    public void validateForConnect(Section section) {
        List<Station> stations = getStations();

        validateAlreadyExistedStations(section, stations);
        validateExistUpStationOrDownStation(section, stations);
    }

    private void validateAlreadyExistedStations(Section section, List<Station> stations) {
        if (isExistedUpStationAndDownStation(section, stations)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean isExistedUpStationAndDownStation(Section section, List<Station> stations) {
        return stations.contains(section.getUpStation())
            && stations.contains(section.getDownStation());
    }

    private void validateExistUpStationOrDownStation(Section section, List<Station> stations) {
        if (isNotExistUpStationOrDownStation(section, stations)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isNotExistUpStationOrDownStation(Section section, List<Station> stations) {
        return !(stations.contains(section.getUpStation())
            || stations.contains(section.getDownStation()));
    }

    public int size() {
        return this.sections.size();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public void remove(Section section) {
        this.sections.remove(section);
    }
}
