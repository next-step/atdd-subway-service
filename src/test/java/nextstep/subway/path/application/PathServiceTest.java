package nextstep.subway.path.application;

import static nextstep.subway.DomainFixtureFactory.createLine;
import static nextstep.subway.DomainFixtureFactory.createLoginMember;
import static nextstep.subway.DomainFixtureFactory.createPath;
import static nextstep.subway.DomainFixtureFactory.createSection;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    TypedQuery<Section> typedQuery;
    @Mock
    private StationService stationService;
    @Mock
    private PathFinder pathFinder;
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    @InjectMocks
    private PathService pathService;

    @BeforeEach
    public void setUp() {
        강남역 = createStation(1L, "강남역");
        양재역 = createStation(2L, "양재역");
        교대역 = createStation(3L, "교대역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = createLine(1L, "신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10), Fare.valueOf(900));
        이호선 = createLine(2L, "이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine(3L, "삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5), Fare.valueOf(500));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("비로그인일 경우 지하철 경로 최단거리 및 요금 검증 테스트")
    @Test
    void findShortestPathNotLogin() {
        ArrayList<StationResponse> stationResponses = Lists.newArrayList(StationResponse.of(남부터미널역),
                StationResponse.of(양재역), StationResponse.of(강남역));
        List<Section> sections = Lists.newArrayList(createSection(신분당선, 강남역, 양재역, Distance.valueOf(10)),
                createSection(이호선, 교대역, 강남역, Distance.valueOf(10)),
                createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)),
                createSection(삼호선, 남부터미널역, 양재역, Distance.valueOf(2)));
        Path path = createPath(Lists.newArrayList(남부터미널역, 양재역, 강남역), Distance.valueOf(15));
        when(stationService.findById(4L)).thenReturn(남부터미널역);
        when(stationService.findById(1L)).thenReturn(강남역);
        when(entityManager.createQuery(
                "select s from Section s join fetch s.upStation join fetch s.downStation join fetch s.line",
                Section.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(sections);
        when(pathFinder.shortestPath(남부터미널역, 강남역)).thenReturn(path);

        PathResponse pathResponse = pathService.findShortestPath(new LoginMember(), 남부터미널역.id(), 강남역.id());
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(15),
                () -> assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2250)
        );
    }

    @DisplayName("로그인일 경우 지하철 경로 최단거리 및 요금 검증 테스트")
    @ParameterizedTest(name = "로그인일 경우 나이가 {0} 일때 최단거리 및 요금 {1} 검증 테스트")
    @CsvSource(value = {"16, 1520", "7, 950"})
    void findShortestPathLogin(int input, int expect) {
        ArrayList<StationResponse> stationResponses = Lists.newArrayList(StationResponse.of(남부터미널역),
                StationResponse.of(양재역), StationResponse.of(강남역));
        List<Section> sections = Lists.newArrayList(createSection(신분당선, 강남역, 양재역, Distance.valueOf(10)),
                createSection(이호선, 교대역, 강남역, Distance.valueOf(10)),
                createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)),
                createSection(삼호선, 남부터미널역, 양재역, Distance.valueOf(2)));
        Path path = createPath(Lists.newArrayList(남부터미널역, 양재역, 강남역), Distance.valueOf(15));
        when(stationService.findById(4L)).thenReturn(남부터미널역);
        when(stationService.findById(1L)).thenReturn(강남역);
        when(entityManager.createQuery(
                "select s from Section s join fetch s.upStation join fetch s.downStation join fetch s.line",
                Section.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(sections);
        when(pathFinder.shortestPath(남부터미널역, 강남역)).thenReturn(path);

        PathResponse pathResponse = pathService.findShortestPath(
                createLoginMember(1L, "email@email.com", Age.valueOf(input)), 남부터미널역.id(), 강남역.id());
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(15),
                () -> assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses),
                () -> assertThat(pathResponse.getFare()).isEqualTo(expect)
        );
    }
}
