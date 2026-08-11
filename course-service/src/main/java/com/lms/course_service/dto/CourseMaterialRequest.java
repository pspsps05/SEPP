package com.lms.course_service.dto;

public class CourseMaterialRequest {

    private Long courseId;
    private String title;
    private String materialType;
    private String materialUrl;
    
    public CourseMaterialRequest() {
    }

    public CourseMaterialRequest(Long courseId, String title, String materialType, String materialUrl) {
        this.courseId = courseId;
        this.title = title;
        this.materialType = materialType;
        this.materialUrl = materialUrl;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }
}