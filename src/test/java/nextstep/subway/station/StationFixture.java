package nextstep.subway.station;

import nextstep.subway.station.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

public class StationFixture {

    private static final Station 영등포구청역 = new Station("영등포구청역");
    private static final Station 당산역 = new Station("당산역");
    private static final Station 합정역 = new Station("합정역");
    private static final Station 홍대입구역 = new Station("홍대입구역");
    private static final Station 여의도역 = new Station("여의도역");
    private static final Station 여의나루역 = new Station("여의나루역");
    private static final Station 서울역 = new Station("서울역");
    private static final Station 시청역 = new Station("시청역");
    private static final Station 남영역 = new Station("남영역");

    private static final Station 강남역 = new Station("강남역");
    private static final Station 양재역 = new Station("양재역");
    private static final Station 교대역 = new Station("교대역");
    private static final Station 남부터미널역 = new Station("남부터미널역");
    private static final Station 간이역 = new Station("간이역");

    public static Station 영등포구청역() {
        return 영등포구청역;
    }

    public static Station 당산역() {
        return 당산역;
    }

    public static Station 합정역() {
        return 합정역;
    }

    public static Station 홍대입구역() {
        return 홍대입구역;
    }

    public static Station 여의도역() {
        return 여의도역;
    }

    public static Station 여의나루역() {
        return 여의나루역;
    }
    public static Station 서울역() {
        ReflectionTestUtils.setField(서울역, "id", 1L);
        return 서울역;
    }
    public static Station 시청역() {
        ReflectionTestUtils.setField(시청역, "id", 2L);
        return 시청역;
    }

    public static Station 남영역() {
        ReflectionTestUtils.setField(남영역, "id", 3L);
        return 남영역;
    }

    public static Station 교대역() {
        ReflectionTestUtils.setField(교대역, "id", 1L);
        return 교대역;
    }
    public static Station 강남역() {
        ReflectionTestUtils.setField(강남역, "id", 2L);
        return 강남역;
    }
    public static Station 양재역() {
        ReflectionTestUtils.setField(양재역, "id", 3L);
        return 양재역;
    }
    public static Station 남부터미널역() {
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);
        return 남부터미널역;
    }

    public static Station 간이역() {
        ReflectionTestUtils.setField(간이역, "id", 5L);
        return 간이역;
    }


}