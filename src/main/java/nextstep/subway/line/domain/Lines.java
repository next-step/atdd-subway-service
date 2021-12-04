package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.line.LineDuplicateException;
import nextstep.subway.common.exception.path.EdgeCreateException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.path.application
 * fileName : Lines
 * author : haedoang
 * date : 2021/12/04
 * description : Line 일급 컬렉션
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        validateDuplicate(lines);
        this.lines = new ArrayList<>(lines);
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    private void validateDuplicate(List<Line> lineList) {
        Set<String> nameSet = lineList.stream()
                .map(Line::getName)
                .collect(Collectors.toSet());

        if (nameSet.size() < lineList.size()) {
            throw new LineDuplicateException();
        }
    }

    public void setEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if(graph.vertexSet().isEmpty()) {
            throw new EdgeCreateException();
        }

        lines.forEach(line -> line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().intValue())
                )
        );
    }

    public List<Line> getList() {
        return lines;
    }
}
