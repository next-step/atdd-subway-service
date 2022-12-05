package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "source_station_id", "target_station_id"})})
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
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
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public void setMember(Member member) {
        if (Objects.isNull(this.member)) {
            this.member = member;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(getMember(), favorite.getMember()) &&
                Objects.equals(getSourceStation(), favorite.getSourceStation()) &&
                Objects.equals(getTargetStation(), favorite.getTargetStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember(), getSourceStation(), getTargetStation());
    }
}
