package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Member extends BaseEntity {
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
    private Favorites favorites = new Favorites();

    public Member() {}

    public Member(String email, String password, Integer age) {
        this.email = Email.from(email);
        this.password = Password.from(password);
        this.age = Age.from(age);
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        this.password.equalsPassword(password);
    }

    public Favorite addFavorite(Station source, Station target) {
        Favorite favorite = Favorite.builder()
                .member(this)
                .source(source)
                .target(target)
                .build();

        this.favorites.add(favorite);
        return favorite;
    }

    public void removeFavorite(Favorite favorite) {
        validateOwner(favorite);
        this.favorites.remove(favorite);
    }

    private void validateOwner(Favorite favorite) {
        if (!this.equals(favorite.getMember())) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_REMOVE_FAVORITE.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public Integer getAge() {
        return age.value();
    }

    public Favorites getFavorites() {
        return favorites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(age, member.age) && Objects.equals(favorites, member.favorites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age, favorites);
    }
}
