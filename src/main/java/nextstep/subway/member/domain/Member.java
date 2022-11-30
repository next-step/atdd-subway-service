package nextstep.subway.member.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.exception.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;

@Entity
public class Member extends BaseEntity {
    public static final String ERROR_MESSAGE_VALID_ID_OR_PASSWORD = "아이디 또는 비밀번호가 일치하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    @Embedded
    private Age age;
    @Embedded
    private Favorites favorites;

    protected Member() {}

    private Member(String email, String password, Integer age, List<Favorite> favorites) {
        this.email = Email.from(email);
        this.password = Password.from(password);
        this.age = Age.from(age);
        this.favorites = Favorites.from(favorites);
    }

    public static Member of(String email, String password, Integer age) {
        return new Member(email, password, age, Collections.emptyList());
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        if (this.password.checkPassword(password)) {
            throw new AuthorizationException(ERROR_MESSAGE_VALID_ID_OR_PASSWORD);
        }
    }

    public void addFavorite(Favorite favorite) {
        favorites.addFavorite(favorite);
        favorite.changeMember(this);
    }

    public Long getId() {
        return id;
    }

    public String emailValue() {
        return email.value();
    }

    public Integer ageValue() {
        return age.value();
    }

    public String passwordValue() {
        return password.value();
    }

    public List<Favorite> favorites() {
        return favorites.list();
    }
}
