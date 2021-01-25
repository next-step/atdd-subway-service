package nextstep.subway.line.exception;

import nextstep.subway.station.domain.Station;

public class CanNotFindPathException extends RuntimeException {

	public CanNotFindPathException(final Station source, final Station target) {
		super(String.format("%s으로부터 %s까지 경로를 찾는데 문제가 발생했습니다..", source.getName(), target.getName()));
	}

}
