package nextstep.subway.path.dto;

import java.util.Arrays;
import java.util.List;

public class PathEdge {

    private final Long sourceVertex;
    private final Long targetVertex;
    private final Integer weight;

    private PathEdge(Long sourceVertex, Long targetVertex, Integer weight) {
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
        this.weight = weight;
    }

    public static PathEdge of(Long sourceVertex, Long targetVertex, Integer weight) {
        return new PathEdge(sourceVertex, targetVertex, weight);
    }

    public List<Long> getAllVertex() {
        return Arrays.asList(getSourceVertex(), getTargetVertex());
    }

    public Long getSourceVertex() {
        return sourceVertex;
    }

    public Long getTargetVertex() {
        return targetVertex;
    }

    public Integer getWeight() {
        return weight;
    }
}
