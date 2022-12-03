package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        member = new Member("abc@gmail.com", "password", 10);
        sourceStation = new Station("강남역");
        targetStation = new Station("양재역");
    }

    @Test
    void 즐겨찾기_생성() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(member, sourceStation, targetStation));

        Favorite favorite = favoriteService.create(1L, new FavoriteRequest(1L, 2L));

        assertThat(favorite).isNotNull();
    }

    @Test
    void 즐겨찾기_생성시_출발역_없으면_예외_발생() {
        when(stationRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }

    @Test
    void 즐겨찾기_생성시_도착역_없으면_예외_발생() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }

    @Test
    void 즐겨찾기_생성시_회원정보_없으면_예외_발생() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(targetStation));
        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Assertions.assertThatThrownBy(() -> favoriteService.create(1L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_MEMBER.message());
    }

    @Test
    void 즐겨찾기_목록_조회() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(Favorite.of(member, sourceStation, targetStation));

        when(favoriteRepository.findByMemberId(any())).thenReturn(favorites);

        List<FavoriteResponse> results = favoriteService.findAllFavorites(1L);

        assertThat(results).isNotEmpty();
    }
}
