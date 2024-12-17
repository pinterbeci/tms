package hu.pinterbeci.tms.model;

import java.time.LocalDateTime;

import hu.pinterbeci.tms.annotations.TMSAnnotationProcessor;
import hu.pinterbeci.tms.annotations.TMSId;

public abstract class BaseModel {

    @TMSId
    private String id;

    private LocalDateTime createdDate;

    private LocalDateTime lastModification;

    private boolean isDeleted;

    public BaseModel() {
        TMSAnnotationProcessor.constructNewInstance(this);
        TMSAnnotationProcessor.initiateTMSIdField(this);
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public void setLastModification(LocalDateTime lastModification) {
        this.lastModification = lastModification;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
