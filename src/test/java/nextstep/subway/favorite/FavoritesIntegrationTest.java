package nextstep.subway.favorite;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.favorites.service.FavoritesService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class FavoritesIntegrationTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private LineService lineService;
    @Autowired
    private FavoritesService favoritesService;
    private Station 양재역;
    private Station 광교역;
    private Member 사용자;
    private Station 강남역;


    @AfterEach
    void tearDown() {
        sectionRepository.deleteAllInBatch();
        favoritesRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @BeforeEach
    void setUp() {
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        강남역 = stationRepository.save(new Station("강남역"));
        Line line = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));
        lineService.addLineStation(line.getId(), new SectionRequest(양재역.getId(), 강남역.getId(), 5));

        사용자 = new Member("hglee", "1234", 30);
        memberRepository.save(사용자);
    }

    @DisplayName("즐겨찾기 생성 요청")
    @Test
    void createFavorites() {
        // when
        Favorites favorites = favoritesService.createFavorite(getFavoriteRequest(양재역, 광교역), 사용자.getId());

        // then
        assertThat(favorites.getDeparture()).isEqualTo(양재역);
        assertThat(favorites.getArrival()).isEqualTo(광교역);
        assertThat(favorites.getMember()).isEqualTo(사용자);
    }

    @DisplayName("즐겨찾기 조회 요청")
    @Test
    void findAllFavorites() {
        // given
        Favorites favorites1 = favoritesService.createFavorite(getFavoriteRequest(양재역, 광교역), 사용자.getId());
        Favorites favorites2 = favoritesService.createFavorite(getFavoriteRequest(양재역, 강남역), 사용자.getId());

        // when
        List<FavoritesResponse> favoritesResponses = favoritesService.findAll(사용자.getId());

        // then
        assertThat(favoritesResponses)
                .extracting("id")
                .contains(favorites1.getId(), favorites2.getId());
    }

    @DisplayName("즐겨찾기 삭제 요청")
    @Test
    void deleteFavorites() {
        // given
        Favorites favorites1 = favoritesService.createFavorite(getFavoriteRequest(양재역, 광교역), 사용자.getId());

        // when
        favoritesService.delete(favorites1.getId());

        // then
        List<FavoritesResponse> favoritesResponses = favoritesService.findAll(사용자.getId());
        assertThat(favoritesResponses).isEmpty();
    }

    private FavoritesRequest getFavoriteRequest(Station source, Station target) {
        return new FavoritesRequest(source.getId(), target.getId());
    }


}
