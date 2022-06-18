package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "SOURCE_ID", nullable = false)
    private Station source;

    @OneToOne
    @JoinColumn(name = "DESTINATION_ID", nullable = false)
    private Station destination;

    @Enumerated(value = EnumType.STRING)
    private DeletedState deleted = DeletedState.NO;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station destination) {
        this.member = member;
        this.source = source;
        this.destination = destination;
        addMember(member);
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

    public Station getDestination() {
        return destination;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void addMember(final Member member) {
        this.member = member;
        if (!member.isContain(this)) {
            member.addFavorite(this);
        }
    }
    public void updateDeletedStateBy(final DeletedState state) {
        this.deleted = state;
    }
}
