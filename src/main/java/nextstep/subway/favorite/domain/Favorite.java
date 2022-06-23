package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
    name = "favorite",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "favorite_unique", columnNames = {"member_id", "up_station_id", "down_station_id"}
        )
    }
)
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    public Favorite() {
    }

    public Favorite(Member member, Station upStation, Station downStation) {
        this.member = member;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(member, favorite.member) && Objects.equals(upStation, favorite.upStation) && Objects.equals(downStation, favorite.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, upStation, downStation);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", member=" + member +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                '}';
    }
}
