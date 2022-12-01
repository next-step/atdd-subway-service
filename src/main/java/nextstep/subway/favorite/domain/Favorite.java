package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        setMember(member);
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
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

    public boolean isId(long favoriteId) {
        return this.id == favoriteId;
    }

    public void setMember(Member newMember) {
        if (Objects.nonNull(member)) {
            member.removeFavorite(this);
        }
        member = newMember;
        if (!member.getFavorites().contains(this)) {
            member.getFavorites().add(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return id.equals(favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
