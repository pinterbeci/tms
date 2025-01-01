package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.annotations.TMSAllowedRoles;
import hu.pinterbeci.tms.enums.HistoryStatus;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.BaseModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseService<S extends BaseModel> {

    private final List<S> modelDataHolder;

    private final Map<String, List<S>> modelHistoryData;

    public BaseService() {
        this.modelDataHolder = new ArrayList<>();
        this.modelHistoryData = new HashMap<>();
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER})
    protected Optional<S> findById(final String id) {
        return modelDataHolder.stream()
                .filter(savedModel -> Objects.equals(savedModel.getId(), id))
                .findFirst();
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected S create(final S newModel) {
        if (Objects.isNull(newModel)) {
            final TMSException modelIsNull = new TMSException("New model creating is not possible, because of the model instance if null",
                    "NEW_MODEL_IS_NULL");
            modelIsNull.printTMSException();
            return null;
        }
        if (Objects.nonNull(newModel.getId())) {
            findById(newModel.getId()).ifPresent(model -> {
                final TMSException modelIsExists =
                        new TMSException("The model creation is not possible, because the model is exists with this id =" + newModel.getId(),
                                "MODEL_IS_EXISTS");
                modelIsExists.printTMSException();
            });
            return null;
        }
        newModel.setCreatedDate(LocalDateTime.now());
        return saveNewItem(newModel);
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected S update(final S model, final String id) {
        final S savedModel = findById(id).orElse(null);

        if (Objects.isNull(savedModel)) {
            final TMSException modelIsNull = new TMSException("The model updating is not possible, because of null value", "NEW_MODEL_IS_NULL");
            modelIsNull.printTMSException();
            return null;
        }

        Arrays.stream(model.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                final Object value = field.get(model);
                if (Objects.nonNull(value)) {
                    savedModel.setLastModification(LocalDateTime.now());
                    savedModel.setHistoryStatus(HistoryStatus.UPDATING);
                    field.set(savedModel, value);
                }
            } catch (final Exception exception) {
                final TMSException invalidModelUpdate = new TMSException("Model update with reflection is thrown exception. Model id = " + id,
                        "INVALID_MODEL_UPDATE", exception);
                invalidModelUpdate.printTMSException();
            }
        });
        fillTaskHistoryData(savedModel);
        return savedModel;
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected void delete(final String id) {
        final S savedModel = findById(id).orElse(null);
        if (Objects.isNull(savedModel)) {
            final TMSException invalidSoftDeleteModel =
                    new TMSException("The model deletion is not possible, because of the model is not exists!", "INVALID_SOFT_DELETE_MODEL");
            invalidSoftDeleteModel.printTMSException();
            return;
        }
        savedModel.setDeleted(true);
        savedModel.setHistoryStatus(HistoryStatus.DELETING);
        fillTaskHistoryData(savedModel);
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER})
    protected List<S> getAll() {
        return this.modelDataHolder.stream()
                .filter(item -> !item.isDeleted())
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected List<S> getAllWithDeleted() {
        return this.modelDataHolder.stream()
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected S saveNewItem(final S model) {
        if (Objects.nonNull(model.getId())) {
            final TMSException modelIsExists =
                    new TMSException("Saving the model is not possible, because the model with this id is exists, the id =" + model.getId(),
                            "MODEL_IS_EXISTS");
            modelIsExists.printTMSException();
            return null;
        }
        model.setHistoryStatus(HistoryStatus.CREATING);
        this.modelDataHolder.add(model);
        fillTaskHistoryData(model);
        return model;
    }

    protected void fillTaskHistoryData(final S model) {
        if (this.modelHistoryData.containsKey(model.getId())) {
            final List<S> storedHistoryData = this.modelHistoryData.get(model.getId());
            storedHistoryData.add(model);
            this.modelHistoryData.put(model.getId(), storedHistoryData);
            return;
        }
        this.modelHistoryData.put(model.getId(), List.of(model));
    }
}
