package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jatin on 12/29/16.
 */
public class TimeData {
    @SerializedName("shortUrlClicks")
    String shortUrlClicks;

    @SerializedName("longUrlClicks")
    String longUrlClicks;

    @SerializedName("referrers")
    List<CountData> referrers;

    @SerializedName("countries")
    List<CountData> countries;

    @SerializedName("browsers")
    List<CountData> browsers;

    @SerializedName("platforms")
    List<CountData> platforms;

    public String getLongUrlClicks() {
        return longUrlClicks;
    }

    public void setLongUrlClicks(String longUrlClicks) {
        this.longUrlClicks = longUrlClicks;
    }

    public String getShortUrlClicks() {
        return shortUrlClicks;
    }

    public void setShortUrlClicks(String shortUrlClicks) {
        this.shortUrlClicks = shortUrlClicks;
    }

    public List<CountData> getReferrers() {
        return referrers;
    }

    public void setReferrers(List<CountData> referrers) {
        this.referrers = referrers;
    }

    public List<CountData> getCountries() {
        return countries;
    }

    public void setCountries(List<CountData> countries) {
        this.countries = countries;
    }

    public List<CountData> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(List<CountData> browsers) {
        this.browsers = browsers;
    }

    public List<CountData> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<CountData> platforms) {
        this.platforms = platforms;
    }
}
