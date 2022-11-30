package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.AbstractLongAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.Fixture.createMember;
import static nextstep.subway.Fixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("즐겾찾기 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    Member member;
    LoginMember loginMember;
    Station 강남역;
    Station 양재역;
    Favorite favorite;

    @BeforeEach
    void setup() {
        member = createMember("email", "password", 30, 1L);
        loginMember = new LoginMember(member.getId(), member.getEmail(), member.getAge());
        강남역 = createStation("강남역", 1L);
        양재역 = createStation("양재역", 2L);
        favorite = new Favorite(member, 강남역, 양재역);
    }

    @DisplayName("즐겨찾기 생성 작업에 성공한다")
    @Test
    void saveFavorite() {
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(favoriteRepository.save(any())).thenReturn(favorite);

        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, favoriteRequest);

        assertAll(
                () -> assertStationId(favoriteResponse.getSourceStation(), 강남역),
                () -> assertStationId(favoriteResponse.getTargetStation(), 양재역)
        );
    }

    private AbstractLongAssert<?> assertStationId(final StationResponse stationResponse, final Station station) {
        return assertThat(stationResponse.getId()).isEqualTo(station.getId());
    }
}
