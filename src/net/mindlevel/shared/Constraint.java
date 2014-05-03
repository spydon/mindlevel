package net.mindlevel.shared;

import java.io.Serializable;
import java.util.Date;

public class Constraint implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token = "";

    /* Search preferences */
    private SearchType type = SearchType.ALL;
    private String username = "";
    private String mission = "";
    private String pictureTitle = "";
    private String sortingColumn = "";
    private boolean validated = true;
    private boolean adult = false;
    private Category category = Category.ALL;
    private Date newerThan;
    private Date olderThan;

    public Constraint(){}

    public Constraint(SearchType type,
                      String token,
                      String username,
                      String mission,
                      String pictureTitle,
                      Category category,
                      boolean validated,
                      boolean adult,
                      String sortingColumn,
                      Date newerThan,
                      Date olderThan) {
        this.setType(type);
        this.setToken(token);
        this.setUsername(username);
        this.setMission(mission);
        this.setPictureTitle(pictureTitle);
        this.setCategory(category);
        this.setValidated(validated);
        this.setAdult(adult);
        this.setSortingColumn(sortingColumn);
        this.setNewerThen(newerThan);
        this.setOlderThen(olderThan);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getNewerThen() {
        return newerThan;
    }

    public void setNewerThen(Date newerThen) {
        this.newerThan = newerThen;
    }

    public Date getOlderThen() {
        return olderThan;
    }

    public void setOlderThen(Date olderThen) {
        this.olderThan = olderThen;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType searchType) {
        this.type = searchType;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSortingColumn() {
        return sortingColumn;
    }

    public void setSortingColumn(String sortingColumn) {
        this.sortingColumn = sortingColumn;
    }
}