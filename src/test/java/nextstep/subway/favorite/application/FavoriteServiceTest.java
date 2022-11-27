package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.fixture.MemberTestFactory;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FavoriteServiceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private String token;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("즐겨찾기를 생성할 수 있다")
    @Test
    void createFavorite() {
        // given
        LoginMember member = MemberTestFactory.createLoginMember(
                memberRepository.save(new Member(EMAIL, PASSWORD, AGE))
        );
        Station sourceStation = stationRepository.save(new Station("강남역"));
        Station targetStation = stationRepository.save(new Station("판교역"));
        FavoriteRequest request = new FavoriteRequest(sourceStation.getId(), targetStation.getId());


        // when
        FavoriteResponse result = favoriteService.saveFavorite(member, request);

        // then
        assertAll(
                () -> assertThat(result.getSource().getId()).isEqualTo(sourceStation.getId()),
                () -> assertThat(result.getTarget().getId()).isEqualTo(targetStation.getId())
        );
    }

    @DisplayName("즐겨찾기 목록을 조회할 수 있다")
    @Test
    void findFavorites() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        Station sourceStation = stationRepository.save(new Station("강남역"));
        Station targetStation = stationRepository.save(new Station("판교역"));
        Favorite expect = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        // when
        List<FavoriteResponse> results = favoriteService.findFavorites(MemberTestFactory.createLoginMember(member));

        // then
        FavoriteResponse result = results.get(0);
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(result.getId()).isEqualTo(expect.getId()),
                () -> assertThat(result.getSource().getId()).isEqualTo(expect.getSource().getId()),
                () -> assertThat(result.getTarget().getId()).isEqualTo(expect.getTarget().getId())
        );
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다")
    @Test
    void deleteFavorite() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        LoginMember loginMember = MemberTestFactory.createLoginMember(member);
        Station sourceStation = stationRepository.save(new Station("강남역"));
        Station targetStation = stationRepository.save(new Station("판교역"));
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        // when
        favoriteService.deleteFavoriteById(loginMember, favorite.getId());

        // then
        assertThat(favoriteRepository.findAll()).hasSize(0);
    }
}
