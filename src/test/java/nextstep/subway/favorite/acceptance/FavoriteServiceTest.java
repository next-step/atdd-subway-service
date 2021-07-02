package nextstep.subway.favorite.acceptance;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
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

    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;


    @BeforeEach
    public void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository);
    }

    @Test
    public void 즐겨찾기_생성_요청() {
        // when
        // 즐겨찾기 생성
        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(new Member(EMAIL, PASSWORD, AGE)));
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(new Station("강남역")));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(new Station("교대역")));

        // then
        // 즐겨찾기 생성이 됨
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        favoriteService.addFavoriteSection(new LoginMember(1L, EMAIL,AGE), favoriteRequest);
    }
}
