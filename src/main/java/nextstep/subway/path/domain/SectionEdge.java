package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    /* source, target, weight은 부모 클래스로부터 상속됨 */
    protected int extraCharge;

    public int getExtraCharge() {
        return extraCharge;
    }
}
