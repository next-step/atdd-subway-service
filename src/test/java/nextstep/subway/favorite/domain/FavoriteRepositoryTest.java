package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
class FavoriteRepositoryTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    private Member 사용자;
    private Station 강남역, 양재역, 방배역, 사당역;
    private Favorite 즐겨찾기1, 즐겨찾기2;

    @Autowired
    public FavoriteRepositoryTest(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @BeforeEach
    void setUp() {
        사용자 = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        방배역 = stationRepository.save(new Station("방배역"));
        사당역 = stationRepository.save(new Station("사당역"));

        즐겨찾기1 = favoriteRepository.save(new Favorite(사용자, 강남역, 양재역));
        즐겨찾기2 = favoriteRepository.save(new Favorite(사용자, 방배역, 사당역));
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // given
        Favorite favorite = new Favorite(사용자, 방배역, 강남역);

        // when
        favorite = favoriteRepository.save(favorite);

        // then
        assertThat(favorite.getId()).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void favorites() {
        // when
        List<Favorite> favorites = favoriteRepository.findByMember(사용자);

        // then
        assertThat(favorites).containsAll(Arrays.asList(즐겨찾기1, 즐겨찾기2));
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void delete() {
        // given
        List<Favorite> favorites = favoriteRepository.findByMember(사용자);

        // when
        favoriteRepository.deleteByIdAndMember(즐겨찾기1.getId(), 사용자);

        // then
        assertThat(favorites).isNotIn(즐겨찾기1);
    }

}