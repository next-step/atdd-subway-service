package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DatabaseCleanup.class)
class FavoriteRepositoryTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private Member 회원;
    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Line 이호선;
    private Line 신분당선;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        회원 = 회원_생성(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        교대역 = 지하철역_생성("교대역");
        이호선 = 지하철_노선_생성("이호선", "bg-green-600", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10);
        즐겨찾기 = 즐겨찾기_생성(회원, 강남역, 양재역);

        entityManager.persist(회원);
        entityManager.persist(강남역);
        entityManager.persist(양재역);
        entityManager.persist(교대역);
        entityManager.persist(이호선);
        entityManager.persist(신분당선);
        favoriteRepository.save(즐겨찾기);
    }

    @Test
    @DisplayName("회원 아이디로 해당 회원의 즐겨찾기 전체 목록 조회")
    void findAllByMemberId() {
        // when
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(회원.getId());

        // then
        assertThat(favorites).hasSize(1);
        assertThat(favorites).contains(즐겨찾기);
    }

    public static Favorite 즐겨찾기_생성(Member member, Station sourceStation, Station targetStation) {
        return new Favorite.Builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .build();
    }

    private Member 회원_생성(String email, String password, Integer age) {
        return new Member.Builder()
                .email(email)
                .password(password)
                .age(age)
                .build();
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

}