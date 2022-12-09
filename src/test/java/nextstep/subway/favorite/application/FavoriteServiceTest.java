package nextstep.subway.favorite.application;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.utils.Message.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    void setUp() {
        member = new Member("yalmung@gmail.com", "password", 10);
        sourceStation = new Station("강남역");
        targetStation = new Station("광교역");
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(member, sourceStation, targetStation));

        Favorite favorite = favoriteService.create(1L, new FavoriteRequest(1L, 2L));

        Assertions.assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기를 생성 시 출발역이 없으면 예외가 발생한다.")
    @Test
    void createExceptionNotExistsSourceStation() {
        when(stationService.findStationById(1L)).thenReturn(null);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        when(memberService.findMemberById(any())).thenReturn(member);

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(FAVORITE_NOT_CONTAIN_STATION);
    }

    @DisplayName("즐겨찾기를 생성 시 도착역이 없으면 예외가 발생한다.")
    @Test
    void createExceptionNotExistsTargetStation() {
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(null);
        when(memberService.findMemberById(any())).thenReturn(member);

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(FAVORITE_NOT_CONTAIN_STATION);
    }

    @DisplayName("즐겨찾기를 생성 시 회원이 없으면 예외가 발생한다.")
    @Test
    void createExceptionNotExistsMember() {
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        when(memberService.findMemberById(any())).thenReturn(null);

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(FAVORITE_NOT_CONTAIN_MEMBER);
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

    @DisplayName("삭제할 즐겨찾기가 없으면 예외가 발생한다.")
    @Test
    void delete() {
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> favoriteService.delete(1L, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(FAVORITE_NOT_EXIST);
    }



}
