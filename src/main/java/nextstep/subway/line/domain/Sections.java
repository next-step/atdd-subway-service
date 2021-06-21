package nextstep.subway.line.domain;

import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<StationResponse> stationResponse() {
        List<StationResponse> result = sections.stream()
                .map(section -> section.getDownStation().toResponse()).collect(Collectors.toList());
        result.addAll(sections.stream()
                .map(section -> section.getUpStation().toResponse()).collect(Collectors.toSet()));
        return result;
    }

    public int size() {
        return sections.size();
    }
}
