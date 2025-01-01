package hu.pinterbeci.tms.model;

import hu.pinterbeci.tms.annotations.TMSId;
import hu.pinterbeci.tms.annotations.processor.TMSModelAnnotationProcessor;
import hu.pinterbeci.tms.enums.HistoryStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseModel {

    @TMSId
    private String id;

    private LocalDateTime createdDate;

    private LocalDateTime lastModification;

    private boolean isDeleted;

    private HistoryStatus historyStatus;

    protected BaseModel() {
        TMSModelAnnotationProcessor.constructNewInstance(this);
        TMSModelAnnotationProcessor.initiateTMSIdField(this);
        this.setHistoryStatus(HistoryStatus.CREATING);
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Objects.isNull(createdDate) ? LocalDateTime.now() : createdDate;
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

    public void setHistoryStatus(final HistoryStatus historyStatus) {
        this.historyStatus = historyStatus;
    }

    public HistoryStatus getHistoryStatus() {
        return historyStatus;
    }
}
