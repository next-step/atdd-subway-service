package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.favorite.domain.Favorite;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private Integer age;
    @Embedded
    private Favorites favorites;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
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

    public List<Favorite> favorites(){
        return favorites.findFavorites();
    }

    public void addFavorite(Favorite favorite) {
        favorites.addFavorite(favorite);
    }

    public void deleteFavorite(Favorite favorite) {
        favorites.deleteFavorite(favorite);
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
    private void validateFavoriteMember(Favorite favorite) {
        if(!favorite.hasSameMember(this)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_SAME_FAVORITE_MEMBER.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(age, member.age);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age);
    }

}
