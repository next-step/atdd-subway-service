package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_favorite_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_favorite_source_station"))
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_favorite_target_station"))
    private Station target;

    protected Favorite() {
    }

    public Favorite(Long id, Member member, Station source, Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.source = sourceStation;
        this.target = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public boolean isMyFavorite(Member member) {
        return this.member.equals(member);
    }
}
