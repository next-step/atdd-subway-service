package nextstep.subway.favorite.application;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.codehaus.groovy.control.messages.ExceptionMessage;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
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
        member = new Member("deokmoon@gmail.com", "password", 10);
        sourceStation = new Station("강남역");
        targetStation = new Station("광교역");
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(member, sourceStation, targetStation));

        Favorite favorite = favoriteService.create(1L, new FavoriteRequest(1L, 2L));

        assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기를 생성 시 출발역이 없으면 예외가 발생한다.")
    @Test
    void makeExceptionWhenSourceStationIsNull() {
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorCode.NO_EXISTS_STATION_IN_FAVORITE.getErrorMessage());
    }

    @DisplayName("즐겨찾기를 생성 시 도착역이 없으면 예외가 발생한다.")
    @Test
    void makeExceptionWhenTargetStationIsNull() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorCode.NO_EXISTS_STATION_IN_FAVORITE.getErrorMessage());
    }

    @DisplayName("즐겨찾기를 생성 시 회원이 없으면 예외가 발생한다.")
    @Test
    void makeExceptionWhenNoMatchMember() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorCode.NO_EXISTS_MEMBER_IN_FAVORITE.getErrorMessage());
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void showFavoriteList() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(Favorite.of(member, sourceStation, targetStation));

        when(favoriteRepository.findByMemberId(any())).thenReturn(favorites);
        List<FavoriteResponse> results = favoriteService.findAllFavorites(1L);

        assertThat(results).isNotEmpty();
    }

    @DisplayName("삭제할 즐겨찾기가 없으면 예외가 발생한다.")
    @Test
    void makeDeleteExceptionWhenNoMatchFavorite() {
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.delete(1L, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorCode.NO_EXISTS_FAVORITE.getErrorMessage());
    }
}
