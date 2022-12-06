package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 관련 서비스 레이어 Stub 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private FavoriteService favoriteService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoriteRepository favoriteRepository;


    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
    }

    @DisplayName("즐겨찾기 생성 서비스 메소드 테스트 with mocking")
    @Test
    void createFavorite() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(stationRepository.findById(anyLong())).thenReturn(Optional.of(new Station("강남역")));
        when(stationRepository.findById(anyLong())).thenReturn(Optional.of(new Station("양재역")));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(new Favorite(new Member(EMAIL, PASSWORD, AGE), null, null));

        FavoriteResponse favoriteResponse = favoriteService.createFavorite(1L, 5L, 3L);

        assertThat(favoriteResponse).isNotNull();
    }
}
