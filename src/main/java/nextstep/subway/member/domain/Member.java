package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.member.exception.FavoriteDuplicatedException;
import nextstep.subway.member.exception.FavoriteNotFoundException;
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

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

    protected Member() {
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

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }

    public void addFavorite(Favorite favorite) {
        validateDuplicate(favorite);
        favorites.add(favorite.by(this));
    }

    private void validateDuplicate(Favorite favorite) {
        if (isExistFavorite(favorite)) {
            throw new FavoriteDuplicatedException();
        }
    }

    private boolean isExistFavorite(Favorite favorite) {
        if (!favorites.isEmpty()) {
            return favorites.stream().allMatch(it -> it.getSourceStation().equals(favorite.getSourceStation()) &&
                    it.getTargetStation().equals(favorite.getTargetStation()));
        }
        return false;
    }

    public void removeFavorite(Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);
        favorites.remove(favorite);
    }

    private Favorite findFavorite(Long id) {
        return favorites.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(FavoriteNotFoundException::new);
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


}
