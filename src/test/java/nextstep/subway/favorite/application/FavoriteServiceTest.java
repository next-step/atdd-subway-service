package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FavoriteServiceTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Member 김민균;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.findByName("강남역");
        양재역 = stationRepository.findByName("양재역");
        교대역 = stationRepository.findByName("교대역");
        김민균 = memberRepository.findByEmail("mkzzang0928@gmail.com").get();
    }

    @AfterEach
    void cleanFavorite() {
        favoriteRepository.deleteAllInBatch();
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // given
        LoginMember loginMember = new LoginMember(김민균.getId(), 김민균.getEmail(), 김민균.getAge());
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertThat(favoriteResponse.getId()).isNotNull();
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findAll() {
        // given
        LoginMember loginMember = new LoginMember(김민균.getId(), 김민균.getEmail(), 김민균.getAge());
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        favoriteService.createFavorite(loginMember, favoriteRequest);
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(양재역.getId(), 교대역.getId());
        favoriteService.createFavorite(loginMember, favoriteRequest2);

        // when
        assertThat(favoriteService.findAllFavorites(loginMember).size()).isEqualTo(2);
    }
}
