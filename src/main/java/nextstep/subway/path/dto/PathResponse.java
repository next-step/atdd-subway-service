package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.path.domain.ChargePolicy;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.List;

public class PathResponse {
	private List<StationResponse> stations;
	private int distance;
	private BigDecimal charge;

	public PathResponse(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public PathResponse(List<StationResponse> stations, int distance, BigDecimal charge) {
		this.stations = stations;
		this.distance = distance;
		this.charge = charge;
	}

	public static PathResponse of(List<Station> stations, int distance, BigDecimal charge) {
		return new PathResponse(StationResponse.of(stations), distance, calculateCharge(charge, distance));
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	private static BigDecimal calculateCharge(BigDecimal charge, int distance) {
		return charge.add(ChargePolicy.valueMatchedByDistance(distance).getCharge(distance));
	}

	public void applyDisAccount(LoginMember loginMember) {
		this.charge = AgeDiscountPolicy.valueByMatchedAge(loginMember).apply(charge);
	}
}
