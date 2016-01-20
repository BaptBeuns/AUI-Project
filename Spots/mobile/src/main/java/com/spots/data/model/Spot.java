package com.spots.data.model;

public class Spot {

	private static int    NO_ID = -1;
	private static int    NO_LONGITUDE = -1;
	private static int    NO_LATITUDE = -1;
	private static boolean NO_FILTER = true;

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

	public void setId(int id) {
		this.id = id;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setFilterWeek(boolean filter) {
		this.filterNotifyWeek = filter;
	}

	public void setFilterWeekEnd(boolean filter) {
		this.filterNotifyWeekEnd = filter;
	}

	public void setFilterEvening(boolean filter) {
		this.filterNotifyEvening = filter;
	}

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean getFilterWeek() {
        return this.filterNotifyWeek;
    }

    public boolean getFilterWeekEnd() {
        return this.filterNotifyWeekEnd;
    }

    public boolean getFilterEvening() {
        return this.filterNotifyEvening;
    }

}