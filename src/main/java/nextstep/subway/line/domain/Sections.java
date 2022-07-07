package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.exception.SectionsNotAddedException;
import nextstep.subway.line.exception.SectionsNotRemovedException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

@Embeddable
public class Sections {
    private final int ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void requestAddSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        checkExistsStation(section);
    }

    private void checkExistsStation(Section section) {
        boolean upMatchesLineUp = stationExistsInUp(section.getUpStation());
        boolean downMatchesLineDown = stationExistsInDown(section.getDownStation());
        boolean matchesAtEnd = stationExistsAtEnd(section);

        if (!upMatchesLineUp && !downMatchesLineDown && !matchesAtEnd) {
            throw new SectionsNotAddedException("구간 중 어떠한 역도 현재 구간에 없습니다.");
        }
        if (upMatchesLineUp && downMatchesLineDown) {
            throw new SectionsNotAddedException("이미 등록된 구간 입니다.");
        }

        addSectionToMatchStation(section, upMatchesLineUp, downMatchesLineDown);
    }

    private void addSectionToMatchStation(Section section, boolean upMatchesLineUp, boolean downMatchsLineDown) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        if (upMatchesLineUp) {
            updateUpStationMatch(section, upStation, downStation, section.getDistance());
        }
        if (downMatchsLineDown) {
            updateDownStatioMatch(section, upStation, downStation, section.getDistance());
        }

        //양쪽 끝에 더해질 경우
        sections.add(section);
    }

    private boolean stationExistsInUp(Station station) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(station));
    }

    private boolean stationExistsInDown(Station station) {
        return sections.stream().anyMatch(section -> section.getDownStation().equals(station));
    }

    private boolean stationExistsAtEnd(Section section) {
        return sections.stream().anyMatch(
                sectionIterate -> sectionIterate.getDownStation().equals(section.getUpStation())
                        || sectionIterate.getUpStation().equals(section.getDownStation()));
    }

    private void updateUpStationMatch(Section section, Station upStation, Station downStation, int distance) {
        sections.stream().filter(sectionIterate -> upStation.equals(sectionIterate.getUpStation())).findFirst()
                .ifPresent(sectionIterate -> sectionIterate.updateUpStation(downStation, distance));
    }

    private void updateDownStatioMatch(Section section, Station upStation, Station downStation, int distance) {
        sections.stream().filter(sectionIterate -> downStation.equals(sectionIterate.getDownStation())).findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }


    public void removeLineStation(Station station, Line line) {
        if (sections.size() <= ONE) {
            throw new SectionsNotRemovedException("현재 구간이 하나밖에 없습니다.");
        }

        Optional<Section> upLineStation = sections.stream().filter(section -> section.getUpStation().equals(station))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(section -> section.getDownStation().equals(station)).findFirst();

        connectFrontBackSection(line, upLineStation, downLineStation);
        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }

    private void connectFrontBackSection(Line line, Optional<Section> upLineStation,
                                         Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();

            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    public void addGraphEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            section.addGraphEdge(graph);
        }
    }

}