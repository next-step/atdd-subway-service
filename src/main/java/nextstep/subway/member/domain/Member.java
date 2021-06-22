package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.application.UnableDeleteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
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
    private Favorites favorites = new Favorites();

    public Member() {
    }

    public Member(Long id) {
        this.id = id;
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
        this.favorites.addFavorite(favorite);
        favorite.setMember(this);
    }

    public void removeFavorite(Favorite favorite) {
        if (!isSameMember(favorite.getMember())) {
            throw new UnableDeleteException("자기 자신의 즐겨찾기만 삭제할수 있습니다.");
        }
        this.favorites.removeFavorite(favorite);
    }

    private boolean isSameMember(Member member) {
        return this.equals(member);
    }

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }
}
