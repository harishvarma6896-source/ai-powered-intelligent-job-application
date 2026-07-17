package com.jobappassistant.dto;

public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String technologies;
    private String link;

    public ProjectDto() {
    }

    public ProjectDto(Long id, String title, String description, String technologies, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
