package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NotFoundStationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 단위테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    public final static String EMAIL = "yong2ss.com";
    public final static String PASSWORD = "12341234";
    public final static Integer AGE = 20;

    private Station 청담역;
    private Station 뚝섬유원지역;
    private Member 회원;
    private Favorite 즐겨찾기;

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void init() {
        favoriteService = new FavoriteService(favoriteRepository, new StationService(stationRepository), memberRepository);
    }

    @Test
    @DisplayName("회원이_출발지와_도착지로_즐겨찾기를_저장하면_정상적으로_등록된다")
    void saveFavorite() {
        //given
        청담역 = new Station(1L, "청담역");
        뚝섬유원지역 = new Station(2L, "뚝섬유원지역");
        회원 = new Member(1L, EMAIL, PASSWORD, AGE);

        //when
        when(stationRepository.findById(1L)).thenReturn(Optional.of(청담역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(뚝섬유원지역));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(회원));
        when(favoriteRepository.save(any())).thenReturn(new Favorite(1L, 회원, 청담역, 뚝섬유원지역));

        FavoriteResponse response = favoriteService.saveFavorite(new LoginMember(1L, EMAIL, AGE),
                                                                    new FavoriteRequest(1L, 2L));

        //then
        assertAll(() -> {
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getSource().getId()).isEqualTo(청담역.getId());
            assertThat(response.getTarget().getId()).isEqualTo(뚝섬유원지역.getId());
        });
    }

    @Test
    @DisplayName("회원 아이디로 저장된 즐겨찾기를 조회한다.")
    void findByMemberId() {
        //given
        청담역 = new Station(1L, "청담역");
        뚝섬유원지역 = new Station(2L, "뚝섬유원지역");
        회원 = new Member(1L, EMAIL, PASSWORD, AGE);
        즐겨찾기 = new Favorite(1L, 회원, 청담역, 뚝섬유원지역);

        //when
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(회원));
        when(favoriteRepository.findByMemberId(anyLong())).thenReturn(Arrays.asList(즐겨찾기));

        List<FavoriteResponse> favoriteResponses = favoriteService.findByMemberId(new LoginMember(1L, EMAIL, AGE));

        //then
        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses.stream()
                                    .filter(favoriteResponse -> favoriteResponse.getId() == 1L)
                                    .findFirst()
                                    .get().getSource()
                                    .getId()).isEqualTo(청담역.getId());
    }
}