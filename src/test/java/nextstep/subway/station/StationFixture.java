package nextstep.subway.station;

import nextstep.subway.station.domain.Station;

public class StationFixture {

    public static final Station 영등포구청역 = new Station("영등포구청역");
    public static final Station 당산역 = new Station("당산역");
    public static final Station 합정역 = new Station("합정역");
    public static final Station 홍대입구역 = new Station("홍대입구역");
    public static final Station 여의도역 = new Station("여의도역");
    public static final Station 여의나루역 = new Station("여의나루역");

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
}