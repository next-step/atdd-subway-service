package nextstep.subway.line.dto;

public class ExistedStationValue {
	boolean isUpStationExisted;
	boolean isDownStationExisted;

	public ExistedStationValue(boolean isUpStationExisted, boolean isDownStationExisted) {
		this.isUpStationExisted = isUpStationExisted;
		this.isDownStationExisted = isDownStationExisted;
	}

	public boolean isUpStationExisted() {
		return isUpStationExisted;
	}

	public boolean isDownStationExisted() {
		return isDownStationExisted;
	}
}
