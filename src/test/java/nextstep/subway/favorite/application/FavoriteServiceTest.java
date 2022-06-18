package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FavoriteServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    StationService stationService;
    @Autowired
    FavoriteService favoriteService;

    private  MemberResponse member;
    private StationResponse 출발역;
    private StationResponse 도착역;
    private StationResponse 출발역2;
    private StationResponse 도착역2;
    private FavoriteRequest favoriteRequest1;
    private FavoriteRequest favoriteRequest2;

    @BeforeEach
    public void setUp(){
        member = memberService.createMember(new MemberRequest("email", "password", 10));
        출발역 = stationService.saveStation(new StationRequest("출발역"));
        도착역 = stationService.saveStation(new StationRequest("도착역"));
        출발역2 = stationService.saveStation(new StationRequest("출발역2"));
        도착역2 = stationService.saveStation(new StationRequest("도착역2"));
        favoriteRequest1 = new FavoriteRequest(출발역.getId(), 도착역.getId());
        favoriteRequest2 = new FavoriteRequest(출발역2.getId(), 도착역2.getId());
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void 즐겨찾기_생성(){
        //when
        FavoriteResponse savedFavorite = favoriteService.saveFavorite(member.getId(), favoriteRequest1);

        //then
        assertAll(
                () -> assertThat(savedFavorite.getId()).isNotNull(),
                () -> assertThat(savedFavorite.getSource().getName()).isEqualTo(출발역.getName()),
                () -> assertThat(savedFavorite.getTarget().getName()).isEqualTo(도착역.getName())
        );
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void 즐겨찾기_조회(){
        //given
        favoriteService.saveFavorite(member.getId(), favoriteRequest1);
        favoriteService.saveFavorite(member.getId(), favoriteRequest2);

        //when
        List<FavoriteResponse> favorites = favoriteService.findFavoritesByMemberId(member.getId());
        FavoriteResponse favorite1 = favorites.get(0);
        FavoriteResponse favorite2 = favorites.get(1);

        //then
        assertAll(
                () -> assertThat(favorites.size()).isEqualTo(2),
                () -> assertThat(favorite1.getSource().getName()).isEqualTo(출발역.getName()),
                () -> assertThat(favorite1.getTarget().getName()).isEqualTo(도착역.getName()),
                () -> assertThat(favorite2.getSource().getName()).isEqualTo(출발역2.getName()),
                () -> assertThat(favorite2.getTarget().getName()).isEqualTo(도착역2.getName())
        );

    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void 즐겨찾기_삭제(){
        //given
        MemberResponse member = memberService.createMember(new MemberRequest("email", "password", 10));
        StationResponse 출발역 = stationService.saveStation(new StationRequest("출발역"));
        StationResponse 도착역 = stationService.saveStation(new StationRequest("도착역"));
        FavoriteRequest request = new FavoriteRequest(출발역.getId(), 도착역.getId());
        FavoriteResponse savedFavorite = favoriteService.saveFavorite(member.getId(), request);

        //when
        favoriteService.deleteFavorite(savedFavorite.getId());

        //then
        assertThrows(IllegalArgumentException.class, () -> favoriteService.findFavoritesByMemberId(savedFavorite.getId()));
    }
}
