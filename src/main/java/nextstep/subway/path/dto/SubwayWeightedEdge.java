package nextstep.subway.path.dto;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.policy.domain.Price;
import nextstep.subway.station.domain.Station;

public class SubwayWeightedEdge extends DefaultWeightedEdge {
    Line line;
    Price lineExtraFare;
    Distance distance;
    Station upStation;
    Station downStation;
    GroupBy groupBy;

    public class GroupBy {
        Station upStation;
        Station downStation;

        public GroupBy(Station upStation, Station downStation) {
            super();
            this.upStation = upStation;
            this.downStation = downStation;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof GroupBy)) {
                return false;
            }
            GroupBy groupBy = (GroupBy) o;
            return Objects.equals(upStation, groupBy.upStation) && Objects.equals(downStation, groupBy.downStation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(upStation, downStation);
        }
    }

    private SubwayWeightedEdge(Section section) {
        super();

        this.line = section.getLine();
        this.lineExtraFare = Price.of(0);

        if (section.getLine() != null) {
            this.lineExtraFare = section.getLine().getExtreFare();
        }

        this.distance = section.getDistance();
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.groupBy = new GroupBy(section.getUpStation(), section.getDownStation());
    }

    public static SubwayWeightedEdge of(Section section) {
        return new SubwayWeightedEdge(section);
    }

    public Line getLine() {
        return this.line;
    }

    public Price getLineExtraFare() {
        return this.lineExtraFare;
    }

    public Distance getDistance() {
        return this.distance;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public GroupBy getGroupBy() {
        return this.groupBy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SubwayWeightedEdge)) {
            return false;
        }
        SubwayWeightedEdge subwayWeightedEdge = (SubwayWeightedEdge) o;
        return Objects.equals(distance, subwayWeightedEdge.distance) && Objects.equals(upStation, subwayWeightedEdge.upStation) && Objects.equals(downStation, subwayWeightedEdge.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, upStation, downStation);
    }
}
