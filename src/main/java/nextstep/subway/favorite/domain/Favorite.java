package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite(){

    }

    public Favorite(Station source, Station target, Member member) {
        this(null, source, target, member);
    }

    public Favorite(Long id, Station sourceStation, Station targetStation, Member member) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;

        if(member != null){
            addBy(member);
        }
    }

    public void addBy(Member member){
        member.addFavorite(this);
    }

    public void deleteBy(Member member){
        member.deleteFavorite(this);
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
