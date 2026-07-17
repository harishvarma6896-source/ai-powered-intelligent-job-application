package com.jobappassistant.dto;

public class SkillDto {
    private Long id;
    private String name;
    private String proficiency;

    public SkillDto() {
    }

    public SkillDto(Long id, String name, String proficiency) {
        this.id = id;
        this.name = name;
        this.proficiency = proficiency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
}
