package nextstep.subway.favorite.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    void setUp() {
        member = new Member("abc@gmail.com", "password", 10);
        sourceStation = new Station("강남역");
        targetStation = new Station("광교역");
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(member, sourceStation, targetStation));

        Favorite favorite = favoriteService.create(1L, new FavoriteRequest(1L, 2L));

        Assertions.assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기를 생성 시 출발역이 없으면 예외가 발생한다.")
    @Test
    void createException1() {
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.STATION_NOT_EXIST);
    }

    @DisplayName("즐겨찾기를 생성 시 도착역이 없으면 예외가 발생한다.")
    @Test
    void createException2() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.STATION_NOT_EXIST);
    }

    @DisplayName("즐겨찾기를 생성 시 회원이 없으면 예외가 발생한다.")
    @Test
    void createException3() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.MEMBER_NOT_EXIST);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAllFavorites() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(Favorite.of(member, sourceStation, targetStation));

        when(favoriteRepository.findByMemberId(any())).thenReturn(favorites);

        List<FavoriteResponse> results = favoriteService.findAllFavorites(1L);

        Assertions.assertThat(results).isNotEmpty();
    }
}
