package nextstep.subway.favorite.service;

import static nextstep.subway.utils.ReflectionHelper.내정보_ID_설정하기;
import static nextstep.subway.utils.ReflectionHelper.역_ID_설정하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {


    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;
    private FavoriteService favoriteService;
    private Member 내정보;
    private Station 시작역;
    private Station 종착역;
    private Favorite 즐겨찾기;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        내정보 = new Member("EMAIL", "PASSWORD", 30);
        시작역 = new Station("시작역");
        종착역 = new Station("종착역");
        내정보_ID_설정하기(1L, 내정보);
        역_ID_설정하기(1L, 시작역);
        역_ID_설정하기(4L, 종착역);

        즐겨찾기 = new Favorite(시작역, 종착역, 내정보);

        favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);
    }


    @Test
    public void 즐겨찾기_생성하기() {
        //given
        when(stationService.findById(1L)).thenReturn(시작역);
        when(stationService.findById(4L)).thenReturn(종착역);
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);
        when(memberService.findById(1L)).thenReturn(내정보);

        FavoriteRequest 즐겨찾기_요청정보 = new FavoriteRequest(1L, 4L);

        //when
        Favorite favorite = favoriteService.saveFavorite(내정보.getId(), 즐겨찾기_요청정보);

        //then
        assertAll(() -> assertThat(favorite.getSource()).isEqualTo(시작역),
            () -> assertThat(favorite.getTarget()).isEqualTo(종착역));
    }

    @Test
    public void 즐겨찾기_목록_조회하기() {
        //given
        when(favoriteRepository.findAllByMember(내정보)).thenReturn(Arrays.asList(즐겨찾기));
        when(memberService.findById(1L)).thenReturn(내정보);

        //when
        List<FavoriteResponse> favoriteList = favoriteService.getFavoriteList(내정보.getId());

        //then
        assertAll(() -> assertThat(favoriteList).extracting("source.name")
                .isEqualTo(Arrays.asList(시작역.getName())),
            () -> assertThat(favoriteList).extracting("target.name")
                .isEqualTo(Arrays.asList(종착역.getName())));
    }
}
