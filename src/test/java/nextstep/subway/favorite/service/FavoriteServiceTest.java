package nextstep.subway.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private StationRepository stationRepository;
    private FavoriteService favoriteService;
    private Member 내정보;
    private Station 시작역;
    private Station 종착역;

    @BeforeEach
    public void init() {
        내정보 = new Member("EMAIL", "PASSWORD", 30);
        시작역 = new Station("시작역");
        종착역 = new Station("종착역");
        when(stationRepository.findById(1L)).thenReturn(Optional.of(시작역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(종착역));
    }


    @Test
    public void 즐겨찾기_생성하기() {
        //given
        FavoriteRequest 즐겨찾기_요청정보 = new FavoriteRequest(1L, 4L);

        //when
        Favorite favorite = favoriteService.saveFavorite(내정보, 즐겨찾기_요청정보);

        //then
        assertAll(() -> assertThat(favorite.getSource()).isEqualTo(시작역),
            () -> assertThat(favorite.getTarget()).isEqualTo(종착역));
    }

    @Test
    public void 즐겨찾기_목록_조회하기() {
        //when
        List<FavoriteResponse> favoriteList = favoriteService.getFavoriteList(내정보);

        //then
        assertAll(() -> assertThat(favoriteList).extracting("source.name").isEqualTo(시작역.getName()),
            () -> assertThat(favoriteList).extracting("target.name").isEqualTo(종착역.getName()));
    }
}
