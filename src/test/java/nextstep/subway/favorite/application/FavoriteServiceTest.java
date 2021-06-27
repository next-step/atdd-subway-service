package nextstep.subway.favorite.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
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

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;

import java.util.Optional;

@DisplayName("FavoriteService 협력객체 이용 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void add() {
        //given
        //when
        when(favoriteRepository.save(any())).thenReturn(new Favorite());
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
        when(stationRepository.findById(any())).thenReturn(Optional.of(new Station()));
        favoriteService.add(new LoginMember(), new FavoriteRequest());
        //then
        verify(favoriteRepository, times(1)).save(any());
    }
}
