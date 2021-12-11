package nextstep.subway.path.domain;

public enum PriceRate {
	MIN_RATE(11, 5, Price.from(100)),
	MAX_RATE(51, 8, Price.from(100));

	private int minDistance;
	private int divideDistance;
	private Price extraPrice;

	PriceRate(int minDistance, int divideDistance, Price extraPrice) {
		this.minDistance = minDistance;
		this.divideDistance = divideDistance;
		this.extraPrice = extraPrice;
	}

	public int getMinDistance() {
		return minDistance;
	}

	public int getDivideDistance() {
		return divideDistance;
	}

	public Price getExtraPrice() {
		return extraPrice;
	}
}
