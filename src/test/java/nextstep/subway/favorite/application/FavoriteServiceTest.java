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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @Test
    void save() {
        Station 강남역 = Station.of("강남역");
        Station 역삼역 = Station.of("역삼역");

        when(stationRepository.findById(anyLong()))
                .thenReturn(Optional.of(강남역), Optional.of(역삼역));
        when(favoriteRepository.save(any()))
                .thenReturn(new Favorite(1L, 강남역, 역삼역));
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(new Member("email", "password", 30)));

        FavoriteResponse response = favoriteService.save(new LoginMember(), new FavoriteRequest(1L, 2L));
        assertThat(response.getSource().getName()).isEqualTo("강남역");
        assertThat(response.getTarget().getName()).isEqualTo("역삼역");
    }

}
