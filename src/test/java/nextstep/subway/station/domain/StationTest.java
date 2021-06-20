package nextstep.subway.station.domain;

import static org.mockito.Mockito.*;

public class StationTest {
	public static Station 강남역 = spy(new Station("강남역"));
	public static Station 양재역 = spy(new Station("양재역"));
	public static Station 양재시민의숲역 = spy(new Station("양재시민의숲역"));
	public static Station 광교역 = spy(new Station("광교역"));
	static {
		when(강남역.getId()).thenReturn(1L);
		when(양재역.getId()).thenReturn(2L);
		when(양재시민의숲역.getId()).thenReturn(3L);
		when(광교역.getId()).thenReturn(4L);
	}
}
