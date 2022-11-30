package nextstep.subway.favorite.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.favorite.exception.FavoriteExceptionCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        validate(member, source, target);

        this.source = source;
        this.target = target;
        this.member = member;
    }

    private void validate(Member member, Station source, Station target) {
        validateMember(member);
        validateStations(source, target);
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException(FavoriteExceptionCode.REQUIRED_MEMBER.getMessage());
        }
    }

    private void validateStations(Station source, Station target) {
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException(FavoriteExceptionCode.REQUIRED_SOURCE.getMessage());
        }

        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(FavoriteExceptionCode.REQUIRED_TARGET.getMessage());
        }

        if(source.equals(target)) {
            throw new IllegalArgumentException(FavoriteExceptionCode.CANNOT_EQUALS_SOURCE_TARGET.getMessage());
        }
    }

    public void checkLoginMember(String email) {
        this.member.checkEmail(email);
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
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
        return Objects.equals(source, favorite.source)
                && Objects.equals(target, favorite.target)
                    && Objects.equals(member, favorite.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, member);
    }
}
