package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

import static nextstep.subway.favorite.exception.FavoriteExceptionCode.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "source_station_id", "target_station_id"}))
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    public Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        validateFavorite(member, sourceStation, targetStation);
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void validateFavorite(Member member, Station sourceStation, Station targetStation) {
        validateMember(member);
        validateSourceStation(sourceStation);
        validateTargetStation(targetStation);
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new FavoriteException(NULL_MEMBER);
        }
    }

    private void validateSourceStation(Station station) {
        if (Objects.isNull(station)) {
            throw new FavoriteException(NULL_SOURCE_STATION);
        }
    }

    private void validateTargetStation(Station station) {
        if (Objects.isNull(station)) {
            throw new FavoriteException(NULL_TARGET_STATION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return id.equals(favorite.id) &&
                member.equals(favorite.member) &&
                sourceStation.equals(favorite.sourceStation) &&
                targetStation.equals(favorite.targetStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, sourceStation, targetStation);
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

    public void validateOwner(Member member) {
        if(!this.member.equals(member)){
            throw new FavoriteException(NOT_OWNER_FAVORITE);
        }
    }
}
