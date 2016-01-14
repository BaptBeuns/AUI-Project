package com.spots.data.model;

public class Spot {

	private static int    NO_ID = -1;
	private static int    NO_LONGITUDE = -1;
	private static int    NO_LATITUDE = -1;
	private static int    NO_FILTER = 0;

	int id;
	double longitude;
	double latitude;
	String name;
	String address;
	boolean filterNotifyWeek;
	boolean filterNotifyWeekEnd;
	boolean filterNotifyEvening;

	public Spot() {
		setId(NO_ID);
		setLongitude(NO_LONGITUDE);
		setLatitude(NO_LATITUDE);
		setName(new String());
		setAddress(new String());
		setFilterWeek(NO_FILTER);
		setFilterWeekEnd(NO_FILTER);
		setFilterWeekEnd(NO_FILTER);
	}

	public setId(int id) {
		this.id = id;
	}

	public setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public setName(String name) {
		this.name = name;
	}

	public setAddress(String address) {
		this.address = address;
	}

	public setFilterWeek(boolean filter) {
		filterNotifyWeek = filter;
	}

	public setFilterWeekEnd(boolean filter) {
		filterNotifyWeekEnd = filter;
	}

	public setFilterEvening(boolean filter) {
		filterNotifyEvening = filter;
	}

}