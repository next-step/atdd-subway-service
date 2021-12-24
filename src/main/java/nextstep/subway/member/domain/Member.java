package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

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
        return this.favorites;
    }

    public void addFavorite(Favorite favorite) {
        if (!this.favorites.contains(favorite)) {
            this.favorites.add(favorite);
        }
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

    private Favorite findFavorite(Favorite favorite) {
        return this.favorites.stream()
                .filter(f -> f.equals(favorite))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기에 대한 권한이 없습니다. 즐겨찾기 ID : " + favorite.getId()));
    }

    public void removeFavorite(Favorite favorite) {
        this.favorites.remove(findFavorite(favorite));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
