package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.station.domain.Station;
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
    private final Favorites favorites = new Favorites();

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
        return this.favorites.getFavorites();
    }

    /**
     * 주어진 역 정보로 자신의 즐겨찾기중에 정보가 일치하는 즐겨찾기를 반환합니다.
     * @param source
     * @param target
     * @return favorite
     */
    public Favorite getFavorite(Station source, Station target) {
        return this.favorites.getFavorite(source, target);
    }

    /**
     * 즐겨찾기를 추가합니다.
     * @param favorite
     */
    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    /**
     * 즐겨찾기를 삭제합니다.
     * @param favoriteId
     */
    public void deleteFavorite(Long favoriteId) {
        this.favorites.remove(favoriteId);
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
}
