package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreatedRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.subway.favorite.domain.Favorite.TARGET_SOURCE_SAME_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 서비스")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private Member member;
    private Station stationA;
    private Station stationB;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        member = memberRepository.save(new Member("email", "password", 20));
        stationA = stationRepository.save(new Station("A"));
        stationB = stationRepository.save(new Station("B"));
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {

        FavoriteResponse response = favoriteService.create(member.getId(), new FavoriteCreatedRequest(stationA.getId(), stationB.getId()));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getSource().getCreatedDate()).isNotNull(),
                () -> assertThat(response.getSource().getName()).isEqualTo("A"),
                () -> assertThat(response.getTarget().getCreatedDate()).isNotNull(),
                () -> assertThat(response.getTarget().getName()).isEqualTo("B")
        );
    }

    @DisplayName("출발역과 도착역이 같을 수 없다.")
    @Test
    void create_fail_sameStation() {
        assertThatThrownBy(() -> favoriteService.create(member.getId(), new FavoriteCreatedRequest(stationA.getId(), stationA.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TARGET_SOURCE_SAME_EXCEPTION_MESSAGE);
    }
}
