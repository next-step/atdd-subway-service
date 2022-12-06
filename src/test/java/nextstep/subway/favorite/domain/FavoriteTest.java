package nextstep.subway.favorite.domain;

import nextstep.subway.JpaEntityTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 관련 도메인 테스트")
class FavoriteTest extends JpaEntityTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    private Station 강남역;
    private Station 양재역;
    private Member 멤버;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        stationRepository.saveAll(Arrays.asList(강남역, 양재역));

        멤버 = new Member("email@email.com", "1234", 12);
        memberRepository.save(멤버);
    }

    @DisplayName("즐겨찾기 도메인 생성 테스트")
    @Test
    void createFavorite() {
        // when
        Favorite 즐겨찾기 = new Favorite(멤버, 강남역, 양재역);
        Favorite favorite = favoriteRepository.save(즐겨찾기);
        flushAndClear();

        // then
        Favorite findFavorite = favoriteRepository.getById(favorite.getId());
        assertThat(findFavorite.getMember()).isEqualTo(멤버);
        assertThat(findFavorite.getSourceStation()).isEqualTo(강남역);
        assertThat(findFavorite.getTargetStation()).isEqualTo(양재역);
    }

    @DisplayName("즐겨찾기 도메인 생성 UK 중복 오류 발생")
    @Test
    void createFavoriteUKException() {
        // when
        Favorite 즐겨찾기 = new Favorite(멤버, 강남역, 양재역);
        favoriteRepository.save(즐겨찾기);
        flushAndClear();

        // then
        assertThatThrownBy(() -> new Favorite(멤버, 강남역, 양재역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("favorite 삭제 테스트")
    @Test
    void deleteFavorte() {
        // given
        Favorite 즐겨찾기 = new Favorite(멤버, 강남역, 양재역);
        favoriteRepository.save(즐겨찾기);
        flushAndClear();

        // when
        즐겨찾기 = favoriteRepository.getById(1L);
        멤버 = memberRepository.getById(1L);
        멤버.deleteFavorite(즐겨찾기);
        favoriteRepository.delete(즐겨찾기);
        flushAndClear();

        // then
        멤버 = memberRepository.getById(1L);
        assertThat(멤버.getFavorites()).isEmpty();
    }

    @DisplayName("다른 멤버의 즐겨찾기를 지우는 경우 Exception 발생")
    @Test
    void deleteFavoriteIsNotOwnerException() {
        // given
        Favorite 즐겨찾기 = new Favorite(멤버, 강남역, 양재역);
        favoriteRepository.save(즐겨찾기);
        flushAndClear();

        // when / then
        Member 신멤버 = new Member("123123", "123132", 11);
        assertThatThrownBy(() -> 신멤버.deleteFavorite(즐겨찾기))
                .isInstanceOf(IllegalArgumentException.class);
    }
}