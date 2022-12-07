package nextstep.subway.member.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.member.exception.NotOwnerException;
import nextstep.subway.station.domain.Station;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @Embedded
    private Favorites favorites = new Favorites();

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Favorites getFavorites() {
        return favorites;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }

    public Favorite addFavorite(Station source, Station target) {
        Favorite favorite = new Favorite(source, target, this);
        this.favorites.add(favorite);
        return favorite;
    }

    public void removeFavorite(Favorite favorite) {
        if (isNotOwnerOfFavorite(favorite)) {
            throw new NotOwnerException();
        }
        this.favorites.delete(favorite);
    }

    private boolean isNotOwnerOfFavorite(Favorite favorite) {
        return !this.id.equals(favorite.getMember().getId());
    }

}
