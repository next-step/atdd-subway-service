package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long loginMemberId;

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

    public Favorite(Long loginMemberId, Station source, Station destination) {
        this.loginMemberId = loginMemberId;
        this.source = source;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public Long getLoginMemberId() {
        return loginMemberId;
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

    public void updateDeletedStateBy(final DeletedState state) {
        this.deleted = state;
    }
}
