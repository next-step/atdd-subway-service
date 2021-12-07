package nextstep.subway.path.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private Station source;
    private Station target;

    public Path(Station source, Station target) {
        checkValidation(source, target);
        this.source = source;
        this.target = target;
    }

    private void checkValidation(Station source, Station target) {
        if (source.equals(target)) {
            throw new InputDataErrorException(InputDataErrorCode.THERE_IS_SAME_STATIONS);
        }
    }
}
