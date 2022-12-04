package nextstep.subway.member.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
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

    @OneToMany(mappedBy = "sourceStation", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

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

    public List<Favorite> getFavorites() {
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

    public void addFavorite(Favorite favorite) {
        checkRedundantFavorite(favorite);
        favorites.add(favorite);
    }

    public void deleteFavorite(Favorite favorite) {
        checkOwner(favorite);
        favorites.remove(favorite);
    }

    private void checkOwner(Favorite favorite) {
        if (favorite.isSameOwner(this)) {
                throw new IllegalArgumentException("본인의 즐겨찾기만 삭제할 수 있습니다.");
        }
    }

    private void checkRedundantFavorite(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException("이미 등록된 즐겨찾기 입니다.");
        }
    }
}
