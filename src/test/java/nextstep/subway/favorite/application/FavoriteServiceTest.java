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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.ofNullable;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private Member 유저;
    private LoginMember 로그인_유저;
    private FavoriteRequest 즐겨찾기_요청;
    private Station 강남역;
    private Station 양재역;
    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        유저 = new Member(EMAIL, PASSWORD, AGE);
        로그인_유저 = new LoginMember(1L, EMAIL, AGE);
        즐겨찾기_요청 = new FavoriteRequest(1L, 2L);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
    }

    @DisplayName("즐겨찾기 목록을 저장한다.")
    @Test
    void saveFavorite() {
        //given
        when(memberRepository.findById(로그인_유저.getId())).thenReturn(ofNullable(유저));
        when(stationRepository.findById(즐겨찾기_요청.getSource())).thenReturn(ofNullable(강남역));
        when(stationRepository.findById(즐겨찾기_요청.getTarget())).thenReturn(ofNullable(양재역));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(new Favorite(유저, 강남역, 양재역));

        //when
        FavoriteResponse saveResponse = favoriteService.saveFavorite(로그인_유저, 즐겨찾기_요청);

        //then
        assertAll(
                () -> assertThat(saveResponse.getSource()).isEqualTo(StationResponse.of(강남역)),
                () -> assertThat(saveResponse.getSource()).isEqualTo(StationResponse.of(양재역))
        );
    }
}
