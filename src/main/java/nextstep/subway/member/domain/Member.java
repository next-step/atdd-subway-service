package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @Embedded
    private FavoriteGroup favorites = new FavoriteGroup(new ArrayList<>());

    public Member() {
    }

    public Member(Long id, String email, String password, Integer age, List<Favorite> favorites) {
        this(email, password, age);
        this.id = id;
        this.favorites = new FavoriteGroup(favorites);
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

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    public void addFavorite(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            favorites.add(favorite);
        }
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }
}
