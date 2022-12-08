package nextstep.subway.favorites.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorites() {

    }

    public Favorites(Station sourceStation, Station targetStation, Member member) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SOURCE_TARGET_EQUAL.getMessage());
        }
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites = (Favorites) o;
        return Objects.equals(id, favorites.id) && Objects.equals(sourceStation, favorites.sourceStation) && Objects.equals(targetStation, favorites.targetStation) && Objects.equals(member, favorites.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStation, targetStation, member);
    }
}
