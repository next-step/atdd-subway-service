package nextstep.subway.station.domain;

import static org.mockito.Mockito.*;

public class StationTest {
    public static Station 강남역 = spy(new Station("강남역"));
    public static Station 양재역 = spy(new Station("양재역"));
    public static Station 양재시민의숲역 = spy(new Station("양재시민의숲역"));
    public static Station 광교역 = spy(new Station("광교역"));
    public static Station 서초역 = spy(new Station("서초역"));
    public static Station 교대역 = spy(new Station("교대역"));
    public static Station 역삼역 = spy(new Station("역삼역"));
    public static Station 고속터미널역 = spy(new Station("고속터미널역"));
    public static Station 남부터미널역 = spy(new Station("남부터미널역"));
    public static Station 도곡역 = spy(new Station("도곡역"));
    public static Station 목동역 = spy(new Station("목동역"));
    public static Station 오목교역 = spy(new Station("오목교역"));
    public static Station 선릉역 = spy(new Station("선릉역"));

    static {
        when(강남역.getId()).thenReturn(1L);
        when(양재역.getId()).thenReturn(2L);
        when(양재시민의숲역.getId()).thenReturn(3L);
        when(광교역.getId()).thenReturn(4L);
        when(서초역.getId()).thenReturn(5L);
        when(교대역.getId()).thenReturn(6L);
        when(역삼역.getId()).thenReturn(7L);
        when(고속터미널역.getId()).thenReturn(8L);
        when(남부터미널역.getId()).thenReturn(9L);
        when(도곡역.getId()).thenReturn(10L);
        when(목동역.getId()).thenReturn(11L);
        when(오목교역.getId()).thenReturn(12L);
        when(선릉역.getId()).thenReturn(13L);
    }
}