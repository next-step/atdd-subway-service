package nextstep.subway.line.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptancePerMethodTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptancePerMethodTest {

    private Station aeogaeStation;
    private Station chungjeongnoStation;
    private Station seodaemunStation;
    private Station gwanghwamunStation;

    @BeforeEach
    void setup() {
        aeogaeStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.애오개역).as(Station.class);
        chungjeongnoStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.충정로역).as(Station.class);
        seodaemunStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.서대문역).as(Station.class);
        gwanghwamunStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.광화문역).as(Station.class);
    }

    @DisplayName("지하철 구간 생성")
    @Test
    void createSection() {
        // given
        Section givenSection = new Section(aeogaeStation, chungjeongnoStation, 10);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), chungjeongnoStation, seodaemunStation, 4);
        ExtractableResponse<Response> sectionResponse = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(sectionResponse, HttpStatus.OK);
    }

    @DisplayName("역 사이에 새로운 역을 등록 (상행역기준)")
    @Test
    void createSectionBetweenStationsByUpStation() {
        // given
        Section givenSection = new Section(aeogaeStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), aeogaeStation, chungjeongnoStation, 3);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("역 사이에 새로운 역을 등록 (하행역기준)")
    @Test
    void createSectionBetweenStationsByDownStation() {
        // given
        Section givenSection = new Section(aeogaeStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), chungjeongnoStation, seodaemunStation, 3);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록")
    @Test
    void createNewStationAsUpStation() {
        // given
        Section givenSection = new Section(chungjeongnoStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), aeogaeStation, chungjeongnoStation, 4);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록")
    @Test
    void createNewStationAsDownStation() {
        // given
        Section givenSection = new Section(chungjeongnoStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), seodaemunStation, gwanghwamunStation, 3);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("예외케이스1 - 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    @Test
    void case1FailsWhenCreateSection() {
        // given
        Section givenSection = new Section(aeogaeStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), aeogaeStation, chungjeongnoStation, 7);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("예외케이스2 - 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    @Test
    void case2FailsWhenCreateSection() {
        // given
        Section givenSection = new Section(aeogaeStation, seodaemunStation, 7);
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, givenSection).as(LineResponse.class);

        // when
        Section section = new Section(lineResponse.toLine(), aeogaeStation, seodaemunStation, 7);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(section);

        // then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("예외케이스3 - 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void case3FailsWhenCreateSection() {
        // given
        ExtractableResponse<Response> createResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, gwanghwamunStation, 10));
        Long lineId = createResponse.jsonPath().getLong("id");
        지하철_구간_등록되어_있음(new Section(new Line(lineId), aeogaeStation, chungjeongnoStation, 3));
        지하철_구간_등록되어_있음(new Section(new Line(lineId), chungjeongnoStation, seodaemunStation, 4));

        // when
        Station yeouidoStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.여의도역).as(Station.class);
        Station yeouinaruStation = StationAcceptanceTest.지하철역_등록되어_있음(StationAcceptanceTest.여의나루역).as(Station.class);
        ExtractableResponse<Response> response = 지하철_구간_등록되어_있음(new Section(new Line(lineId), yeouidoStation, yeouinaruStation, 7));

        // then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간에서 중간역 삭제")
    @Test
    void deleteMiddleStation() {
        // Given
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, seodaemunStation, 7)).as(LineResponse.class);
        지하철_구간_등록되어_있음(new Section(lineResponse.toLine(), aeogaeStation, chungjeongnoStation, 3));

        // When
        ExtractableResponse<Response> response = delete("/lines/" + lineResponse.getId() + "/sections?stationId=" + chungjeongnoStation.getId());

        // Then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("구간에서 상행역 삭제")
    @Test
    void deleteUpStation() {
        // Given
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, chungjeongnoStation, 3)).as(LineResponse.class);
        지하철_구간_등록되어_있음(new Section(lineResponse.toLine(), chungjeongnoStation, seodaemunStation, 4));

        // When
        ExtractableResponse<Response> response = delete("/lines/" + lineResponse.getId() + "/sections?stationId=" + aeogaeStation.getId());

        // Then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("구간에서 하행역 삭제")
    @Test
    void deleteDownStation() {
        // Given
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, chungjeongnoStation, 3)).as(LineResponse.class);
        지하철_구간_등록되어_있음(new Section(lineResponse.toLine(), chungjeongnoStation, seodaemunStation, 4));

        // When
        ExtractableResponse<Response> response = delete("/lines/" + lineResponse.getId() + "/sections?stationId=" + seodaemunStation.getId());

        // Then
        assertResponseCode(response, HttpStatus.OK);
    }

    @DisplayName("노선에 등록되지 않은 역을 삭제 시도시 예외발생")
    @Test
    void failCase1_when_deleteStation() {
        // Given
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, chungjeongnoStation, 3)).as(LineResponse.class);
        지하철_구간_등록되어_있음(new Section(lineResponse.toLine(), chungjeongnoStation, seodaemunStation, 4));

        // When
        ExtractableResponse<Response> response = delete("/lines/" + lineResponse.getId() + "/sections?stationId=" + gwanghwamunStation.getId());

        // Then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간이 1개만 남았을 경우 삭제 시도시 예외발생")
    @Test
    void failCase2_when_deleteStation() {
        // Given
        LineResponse lineResponse = LineAcceptanceTest.지하철_노선_등록되어_있음_두_종점역_포함(LineAcceptanceTest.line5, new Section(aeogaeStation, chungjeongnoStation, 3)).as(LineResponse.class);

        // When
        ExtractableResponse<Response> response = delete("/lines/" + lineResponse.getId() + "/sections?stationId=" + aeogaeStation.getId());

        // Then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    public static ExtractableResponse<Response> 지하철_구간_등록되어_있음(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", section.getUpStation().getId());
        params.put("downStationId", section.getDownStation().getId());
        params.put("distance", section.getDistanceWeight());

        return post(params, "/lines/" + section.getLine().getId() + "/sections");
    }
}
