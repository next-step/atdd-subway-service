package nextstep.subway.favorite.application;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.favorite.domain.FavoriteFixture.즐겨찾기1;
import static nextstep.subway.favorite.domain.FavoriteFixture.즐겨찾기2;
import static nextstep.subway.member.domain.MemberFixture.회원1;
import static nextstep.subway.station.StationFixture.서울역;
import static nextstep.subway.station.StationFixture.시청역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    FavoriteService favoriteService;

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void 즐겨찾기_생성_테스트() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(서울역().getId(), 시청역().getId());

        given(stationService.findById(서울역().getId())).willReturn(서울역());
        given(stationService.findById(시청역().getId())).willReturn(시청역());
        given(favoriteRepository.save(any())).willReturn(즐겨찾기1());

        // when
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(회원1().getId(), favoriteRequest);

        // then
        assertAll(
                () -> verify(favoriteRepository).save(any()),
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(서울역().getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(시청역().getName())
        );
    }

    @DisplayName("출발역과 도착역이 같은 즐겨찾기를 생성한다.")
    @Test
    void 출발역과_도착역이_같은_즐겨찾기_생성_테스트() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(서울역().getId(), 서울역().getId());

        given(stationService.findById(서울역().getId())).willReturn(서울역());

        // when & then
        assertThatThrownBy(
                () -> favoriteService.addFavorite(회원1().getId(), favoriteRequest)
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("기존에 등록된 즐겨찾기를 등록한다.")
    @Test
    void 기존에_등록된_즐겨찾기_등록_테스트() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(서울역().getId(), 시청역().getId());

        given(favoriteRepository.findAll()).willReturn(Arrays.asList(즐겨찾기1(), 즐겨찾기2()));
        given(stationService.findById(서울역().getId())).willReturn(서울역());
        given(stationService.findById(시청역().getId())).willReturn(시청역());

        // when & then
        assertThatThrownBy(
                () -> favoriteService.addFavorite(회원1().getId(), favoriteRequest)
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void 즐겨찾기_목록_조회_테스트() {
        // given
        given(favoriteRepository.findByMemberId(회원1().getId())).willReturn(Arrays.asList(즐겨찾기1(), 즐겨찾기2()));

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.retrieveFavorites(회원1().getId());

        //
        assertAll(
                () -> assertThat(favoriteResponses).hasSize(2),
                () -> assertThat(favoriteResponses.stream()
                        .map(it -> it.getId())
                        .collect(Collectors.toList())).containsExactly(즐겨찾기1().getId(), 즐겨찾기2().getId())
        );
    }
}
