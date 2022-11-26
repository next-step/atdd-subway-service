package nextstep.subway.member.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    private String password;
    @Embedded
    private Age age;
    @Embedded
    private Favorites favorites;

    protected Member() {
    }

    private Member(String email, String password, int age, List<Favorite> favorites) {
        this.email = Email.from(email);
        this.password = password;
        this.age = Age.from(age);
        this.favorites = Favorites.from(favorites);
    }

    public static Member of(String email, String password, int age) {
        return new Member(email, password, age, Collections.emptyList());
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

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Age getAge() {
        return age;
    }
}
