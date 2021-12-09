package nextstep.subway.path.domain;

import nextstep.subway.path.domain.price.PathPrice;
import nextstep.subway.path.domain.route.PathRoute;

public class Path {

	private PathRoute pathRoute;
	private PathPrice pathPrice;

	public Path(PathRoute pathRoute, PathPrice pathPrice) {
		this.pathRoute = pathRoute;
		this.pathPrice = pathPrice;
	}

	public PathRoute getPathRoute() {
		return pathRoute;
	}

	public int getPrice() {
		return pathPrice.getPrice();
	}
}
