package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.FavoriteSection;
import nextstep.subway.favorite.domain.FavoriteSectionRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private static final String EMAIL = "email@test.com";
    private static final String PASSWORD = "1234";
    private static final int AGE = 19;

    private static final Member MEMBER = new Member(1L, EMAIL, PASSWORD, AGE);
    private static final Station 강남역 = new Station("강남역");
    private static final Station 교대역 = new Station("교대역");

    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteSectionRepository favoriteSectionRepository;

    @BeforeEach
    public void setUp() {
        favoriteService = new FavoriteService(favoriteSectionRepository, memberRepository, stationRepository);

        // given
        // 회원과 역이 등록되어 있음
        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(MEMBER));
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(교대역));
    }

    @Test
    public void 즐겨찾기_생성_요청() {
        // when
        // 즐겨찾기 생성이 됨
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        favoriteService.addFavoriteSection(1L, favoriteRequest);

        // then
        // 즐겨찾기 저장 됨
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteSection(1L);
        assertThat(favoriteResponses.size()).isNotZero();
    }

    @Test
    public void 즐겨찾기_조회_요청() {
        // when
        // 즐겨찾기 생성이 됨
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        favoriteService.addFavoriteSection(1L, favoriteRequest);

        // and
        // 즐겨찾기 조회 요청
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteSection(1L);

        // then
        // 즐겨찾기 조회 됨
        assertThat(favoriteResponses.get(0).getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponses.get(0).getTarget().getName()).isEqualTo("교대역");
    }
}
