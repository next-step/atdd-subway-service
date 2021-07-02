package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.Excetion.AuthorizationException;
import nextstep.subway.favorite.domain.FavoriteSection;
import nextstep.subway.favorite.domain.FavoriteSections;
import nextstep.subway.station.domain.Station;
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
    private FavoriteSections favoriteSections = new FavoriteSections();

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

    public void addFavoriteSection(Station source, Station target) {
        this.favoriteSections.addFavoriteSection(this, new FavoriteSection(source, target));
    }

    public FavoriteSections getFavoriteSections() {
        return this.favoriteSections;
    }
}
