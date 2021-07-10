package nextstep.subway.line.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class LineStation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    protected LineStation() {
    }

    public LineStation(Line line, Station station) {
        this.line = line;
        this.station = station;
    }

    public boolean isSameLineAndStation(LineStation lineStation) {
        return this.line.equals(lineStation.line) && this.station.equals(lineStation.station);
    }
}
