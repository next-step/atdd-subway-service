package nextstep.subway.member.domain;

import javax.persistence.Embedded;
import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
        Favorite favorite = new Favorite(this, source, target);
        this.favorites.add(favorite);

        return favorite;
    }

    public Favorites getFavorites() {
        return favorites;
    }

    public void removeFavorite(Favorite favorite) {
        verifyFavorite(favorite);
        this.favorites.remove(favorite);
    }

    private void verifyFavorite(Favorite favorite) {
        if (!this.equals(favorite.getMember())) {
            throw new AuthorizationException();
        }
    }
}
