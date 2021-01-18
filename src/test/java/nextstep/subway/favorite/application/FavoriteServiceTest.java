package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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
    ;
    private Line 신분당선;
    private Member 김민균;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.findByName("강남역");
        양재역 = stationRepository.findByName("양재역");
        신분당선 = lineRepository.findByName("신분당선");
        김민균 = memberRepository.findByEmail("mkzzang0928@gmail.com").get();
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
}
