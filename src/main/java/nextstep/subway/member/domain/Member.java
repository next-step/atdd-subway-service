package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Favorites favorites = new Favorites();

    private String email;
    private String password;
    private Integer age;

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

    public void addFavorite(Favorite favorite) {
        favorites.addFavorite(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.removeFavorite(favorite);
    }

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
