package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    public static final int FREE_USER_UNDER = 5;
    public static final int FREE_USER_OVER = 65;
    public static final int ADULT_USER_UPPER_BOUND = 64;
    public static final int ADULT_USER_LOWER_BOUND = 19;
    public static final int YOUTH_USER_UPPER_BOUND = 18;
    public static final int YOUTH_USER_LOWER_BOUND = 13;
    public static final int CHILD_USER_UPPER_BOUND = 12;
    public static final int CHILD_USER_LOWER_BOUND = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private Integer age;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, orphanRemoval = true, fetch = FetchType.LAZY)
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

    public boolean isFree() {
        return age <= FREE_USER_UNDER || age >= FREE_USER_OVER;
    }

    public boolean isAdult() {
        return id == null || (age <= ADULT_USER_UPPER_BOUND && age >= ADULT_USER_LOWER_BOUND);
    }

    public boolean isYouth() {
        return age >= YOUTH_USER_LOWER_BOUND && age <= YOUTH_USER_UPPER_BOUND;
    }

    public boolean isChild() {
        return age >= CHILD_USER_LOWER_BOUND && age <= CHILD_USER_UPPER_BOUND;
    }

    public List<Favorite> favorites() {
        return favorites;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        favorites.remove(favorite);
    }

}
