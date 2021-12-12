package nextstep.subway.member.step;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;

import nextstep.subway.line.dto.line.LineRequest;
import nextstep.subway.line.dto.line.LineResponse;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteLineAcceptanceFixtures {

    public static LineResponse 신분당선;
    public static StationResponse 강남역;
    public static StationResponse 양재역;
    public static StationResponse 정자역;
    public static StationResponse 광교역;

    public static void 노선등록되어있음() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
            10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

}
