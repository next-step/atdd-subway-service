package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    private LineService lineService;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Station 정자역;

    private Station 야탑역;
    private Station 모란역;
    private Station 수진역;
    private Station 태평역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");

        야탑역 = new Station("야탑역");
        모란역 = new Station("모란역");
        수진역 = new Station("수진역");
        태평역 = new Station("태평역");

        lineService = new LineService(lineRepository, new StationService(stationRepository));
    }

    @Test
    @DisplayName("저장을 하면 정렬된 역의 Response가 같이 나온다")
    void 저장을_하면_정렬된_역의_Response가_같이_나온다() {
        // given
        stationRepository.saveAll(Arrays.asList(양재역, 판교역));
        LineRequest lineRequest = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertThat(lineResponse.getName()).isEqualTo(lineRequest.getName());
        assertThat(lineResponse.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(lineResponse.getStations())
                .map(StationResponse::getId)
                .containsExactly(양재역.getId(), 판교역.getId());
    }

    @Test
    @DisplayName("검색을 하면 정렬된 역의 Response가 같이 나온다")
    void 검색을_하면_정렬된_역의_Response가_같이_나온다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));

        LineRequest lineRequest = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);

        LineResponse lineResponse = lineService.saveLine(lineRequest);
        lineService.addLineStation(lineResponse.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));
        lineService.addLineStation(lineResponse.getId(), new SectionRequest(판교역.getId(), 정자역.getId(), 3));

        // when
        LineResponse lineResponseById = lineService.findLineResponseById(lineResponse.getId());

        // then
        assertThat(lineResponseById.getName()).isEqualTo(lineRequest.getName());
        assertThat(lineResponseById.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(lineResponseById.getStations())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 양재역.getId(), 판교역.getId(), 정자역.getId());
    }

    @Test
    @DisplayName("findAll로 검색을 하면 정렬된 역의 Response가 같이 나온다")
    void findAll_로_검색을_하면_정렬된_역의_Response가_같이_나온다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));
        stationRepository.saveAll(Arrays.asList(야탑역, 모란역, 수진역, 태평역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);
        LineRequest 분당_요청 = new LineRequest("분당선", "노란색", 야탑역.getId(), 수진역.getId(), 3);

        LineResponse 신분당_웅답 = lineService.saveLine(신분당_요청);
        lineService.addLineStation(신분당_웅답.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));
        lineService.addLineStation(신분당_웅답.getId(), new SectionRequest(판교역.getId(), 정자역.getId(), 3));

        LineResponse 분당_웅답 = lineService.saveLine(분당_요청);
        lineService.addLineStation(분당_웅답.getId(), new SectionRequest(모란역.getId(), 수진역.getId(), 1));
        lineService.addLineStation(분당_웅답.getId(), new SectionRequest(수진역.getId(), 태평역.getId(), 3));

        // when
        List<LineResponse> lineResponses = lineService.findLines();

        // then
        assertAll(
                () -> assertThat(lineResponses.get(0).getName()).isEqualTo(신분당_요청.getName()),
                () -> assertThat(lineResponses.get(0).getColor()).isEqualTo(신분당_요청.getColor()),
                () -> assertThat(lineResponses.get(0).getStations())
                        .map(StationResponse::getId)
                        .containsExactly(강남역.getId(), 양재역.getId(), 판교역.getId(), 정자역.getId())
        );
        assertAll(
                () -> assertThat(lineResponses.get(1).getName()).isEqualTo(분당_요청.getName()),
                () -> assertThat(lineResponses.get(1).getColor()).isEqualTo(분당_요청.getColor()),
                () -> assertThat(lineResponses.get(1).getStations())
                        .map(StationResponse::getId)
                        .containsExactly(야탑역.getId(), 모란역.getId(), 수진역.getId(), 태평역.getId())
        );
    }

    @Test
    @DisplayName("구간이 한개만 있으면 RuntimeException이 발생한다")
    void 구간이_한개만_있으면_RuntimeException이이_발생한다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);

        LineResponse 신분당_응답 = lineService.saveLine(신분당_요청);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> lineService.removeLineStation(신분당_응답.getId(), 양재역.getId()));
    }

    @Test
    @DisplayName("역을 삭제를 하면 새로운 구간이 생성되어야 한다")
    void 역을_삭제를_하면_새로운_구간이_생성되어야_한다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);

        LineResponse 신분당_응답 = lineService.saveLine(신분당_요청);
        lineService.addLineStation(신분당_응답.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));

        // when
        lineService.removeLineStation(신분당_응답.getId(), 양재역.getId());

        // then
        LineResponse lineResponse = lineService.findLineResponseById(신분당_응답.getId());

        assertThat(lineResponse.getStations())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 판교역.getId());
    }

    @Test
    @DisplayName("이미 등록된 역들을 등록하면 RuntimeException이 발생한다")
    void 이미_등록된_역들을_등록하면_RuntimeException이_발생한다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 정자역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 강남역.getId(), 정자역.getId(), 3);
        LineResponse 신분당_응답 = lineService.saveLine(신분당_요청);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> lineService.addLineStation(신분당_응답.getId(), new SectionRequest(강남역.getId(), 정자역.getId(), 3)))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("노선에 등록되지 않은 역을 연결하려 할 경우 RuntimeException이 발생한다")
    void 노선에_등록되지_않은_역을_연결하려_할_경우_RuntimeException이_발생한다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 강남역.getId(), 정자역.getId(), 3);
        LineResponse 신분당_응답 = lineService.saveLine(신분당_요청);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> lineService.addLineStation(신분당_응답.getId(), new SectionRequest(양재역.getId(), 판교역.getId(), 3)))
                .withMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("노선에 아무 역도 없을경우 등록된다")
    void 노선에_아무_역도_없을경우_등록된다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역));
        Line save = lineRepository.save(new Line("신분당선", "빨간색"));

        // when
        lineService.addLineStation(save.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));

        // then
        LineResponse lineById = lineService.findLineResponseById(save.getId());
        assertThat(lineById.getStations())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 양재역.getId());
    }

    @Test
    @DisplayName("상행선에 연결할 수 있다")
    void 상행선에_연결할_수_있다() {
        // given
        stationRepository.saveAll(Arrays.asList(강남역, 양재역, 판교역, 정자역));

        LineRequest 신분당_요청 = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);
        LineResponse 신분당_응답 = lineService.saveLine(신분당_요청);

        // when
        lineService.addLineStation(신분당_응답.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));

        // then
        LineResponse lineResponse = lineService.findLineResponseById(신분당_응답.getId());

        assertThat(lineResponse.getStations())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 양재역.getId(), 판교역.getId());

    }
}