package nextstep.subway.path.domain;

public interface GraphEdgeWeight {
    PathVertex getSourceVertex();
    PathVertex getTargetVertex();
    Double getWeight();
}
