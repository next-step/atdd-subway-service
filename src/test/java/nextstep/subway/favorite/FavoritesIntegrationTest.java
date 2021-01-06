package nextstep.subway.favorite;

import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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


    @AfterEach
    void tearDown() {
        sectionRepository.deleteAllInBatch();
        favoritesRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("즐겨찾기 생성 요청")
    @Test
    void createFavorites() {
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Line line = lineRepository.save(new Line("신분당선", "red lighten-1", 양재역, 광교역, 10));
        lineService.addLineStation(line.getId(), new SectionRequest(양재역.getId(), 강남역.getId(), 5));

        Member member = new Member("hglee", "1234", 30);
        memberRepository.save(member);

        Favorite favorite = favoritesService.createFavorite(new FavoritesRequest(양재역.getId(), 광교역.getId()), member.getId());

        assertThat(favorite.getDeparture()).isEqualTo(양재역);
        assertThat(favorite.getArrival()).isEqualTo(광교역);
        assertThat(favorite.getMember()).isEqualTo(member);
    }
}
