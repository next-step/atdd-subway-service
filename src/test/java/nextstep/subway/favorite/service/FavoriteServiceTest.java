package nextstep.subway.favorite.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@DisplayName("FavoriteService 테스트")
class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Station 양평역 = new Station("양평역");
    private Station 영등포역 = new Station("영등포역");
    private Member 회원_죠르디 = new Member("jordy-torvalds@jordy.com", "jordy", 29);
    private LoginMember 로그인_죠르디 = new LoginMember(1L, "jordy-torvalds@jordy.com", 29);

    private FavoriteResponse insertResponse;

    @BeforeEach
    void setUp() {
        stationRepository.save(양평역);
        stationRepository.save(영등포역);
        memberRepository.save(회원_죠르디);

        로그인_죠르디 = new LoginMember(회원_죠르디.getId(), 회원_죠르디.getEmail(), 회원_죠르디.getAge());

        // given
        favoriteService = new FavoriteService(new StationService(stationRepository), favoriteRepository);
        FavoriteRequest favoriteRequest = new FavoriteRequest(양평역.getId(), 영등포역.getId());

        insertResponse = favoriteService.insertFavorite(로그인_죠르디, favoriteRequest);
    }

    @Test
    @DisplayName("즐겨찾기 생성 및 검증")
    void createFavorite() {
        // then
        assertAll(
                () -> assertThat(insertResponse.getSource().getId()).isEqualTo(양평역.getId()),
                () -> assertThat(insertResponse.getTarget().getId()).isEqualTo(영등포역.getId())
        );
    }

    @Test
    @DisplayName("즐겨찾기 조회 및 검증")
    void findFavorite() {
        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorite(로그인_죠르디);

        // then
        favoriteResponses.forEach(favoriteResponse -> {
            assertAll(
                    () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(양평역.getId()),
                    () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(양평역.getName()),
                    () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(영등포역.getId()),
                    () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(영등포역.getName())
            );
        });
    }

    @Test
    @DisplayName("즐겨찾기 삭제 및 검증")
    void deleteFavorite() {
        // when, then
        assertDoesNotThrow(() -> favoriteService.deleteById(로그인_죠르디, insertResponse.getId()));
    }
}
