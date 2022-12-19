package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_favorite_to_member"), nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_sourcestation"), nullable = false)
    private Station sourceStation;
    @ManyToOne
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_targetstation"), nullable = false)
    private Station targetStation;

    protected Favorite() {
    }

    private Favorite(Member member, Station sourceStation, Station targetStation) {
        validateSourceAndTargetEquals(sourceStation, targetStation);
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
    }

    private void validateSourceAndTargetEquals(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SAME_START_END_STATION.getMessage());
        }
    }

    public boolean hasSameMember(Favorite favorite) {
        return this.member.equals(favorite.member);
    }

    public boolean hasSameMember(Member member) {
        return this.member.equals(member);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return Objects.equals(getId(), favorite.getId()) && Objects.equals(member, favorite.member)
                && Objects.equals(getSourceStation(), favorite.getSourceStation()) && Objects.equals(
                getTargetStation(), favorite.getTargetStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), member, getSourceStation(), getTargetStation());
    }
}
