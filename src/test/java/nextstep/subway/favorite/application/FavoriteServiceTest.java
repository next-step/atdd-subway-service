package nextstep.subway.favorite.application;

import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FavoriteServiceTest {

    Long sourceId;
    Long targetId;
    @Autowired
    private FavoriteService favoriteService;
    @MockBean
    private FavoriteRepository favoriteRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private StationRepository stationRepository;
    private Station source;
    private Station target;
    private Long memberId;
    private Member member;
    private Long favoriteId;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository,
            stationRepository);
        memberId = 1L;
        member = new Member(EMAIL, PASSWORD, AGE);
        sourceId = 1L;
        targetId = 2L;
        source = new Station("강남역");
        target = new Station("남부터미널역");
        favoriteId = 1L;
        favorite = new Favorite(member, source, target);
    }

    @Test
    @DisplayName("즐겨찾기 등록")
    void saveFavorite() {
        // given;
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(stationRepository.findById(sourceId)).willReturn(Optional.of(source));
        given(stationRepository.findById(targetId)).willReturn(Optional.of(target));
        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(memberId,
            favoriteRequest);

        // then
        compareSourceAndTarget(StationResponse.of(favoriteResponse.getSource()),
            StationResponse.of(favoriteResponse.getTarget()), source, target);
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void findFavorites() {
        // given
        given(favoriteRepository.findAllByMemberId(any())).willReturn(
            Collections.singletonList(favorite));

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(favoriteId);

        // then
        assertAll(
            () -> assertThat(favoriteResponses).hasSize(1),
            () -> compareSourceAndTarget(StationResponse.of(favoriteResponses.get(0).getSource()),
                StationResponse.of(favoriteResponses.get(0).getTarget()), source, target)
        );
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        // given
        given(favoriteRepository.findById(any())).willReturn(Optional.of(favorite));

        // when
        favoriteService.deleteFavorite(favoriteId);

        // then
        then(favoriteRepository)
            .should()
            .delete(favorite);
    }

    private void compareSourceAndTarget(StationResponse source, StationResponse target,
        Station expectedSource, Station expectedTarget) {
        assertAll(
            () -> assertThat(source.getId()).isEqualTo(expectedSource.getId()),
            () -> assertThat(source.getName()).isEqualTo(expectedSource.getName()),
            () -> assertThat(target.getId()).isEqualTo(expectedTarget.getId()),
            () -> assertThat(target.getName()).isEqualTo(expectedTarget.getName())
        );
    }
}
