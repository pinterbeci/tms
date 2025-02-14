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
            throw new TMSException("New model creating is not possible, because of the model instance if null",
                    "NEW_MODEL_IS_NULL");
        }
        if (Objects.nonNull(newModel.getId())) {
            findById(newModel.getId()).ifPresent(model -> {
                throw new TMSException("The model creation is not possible, because the model is exists with this id =" + newModel.getId(),
                        "MODEL_IS_EXISTS");
            });
            return null;
        }
        return saveNewItem(newModel);
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected S update(final S model, final String id) {
        final S savedModel = findById(id).orElse(null);

        if (Objects.isNull(savedModel)) {
            throw new TMSException("The model updating is not possible, because of null value", "NEW_MODEL_IS_NULL");
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
                throw new TMSException("Model update with reflection is thrown exception. Model id = " + id,
                        "INVALID_MODEL_UPDATE", exception);
            }
        });
        fillTaskHistoryData(savedModel);
        return savedModel;
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected void delete(final String id) {
        final S savedModel = findById(id).orElse(null);
        if (Objects.isNull(savedModel)) {
            throw new TMSException("The model deletion is not possible, because of the model is not exists!", "INVALID_SOFT_DELETE_MODEL");

        }
        savedModel.setDeleted(true);
        savedModel.setHistoryStatus(HistoryStatus.DELETING);
        fillTaskHistoryData(savedModel);
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER})
    public List<S> getAll() {
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
        final S savedItem = findById(model.getId()).orElse(null);
        if (Objects.nonNull(savedItem)) {
            throw new TMSException("Saving the model is not possible, because the model with this id is exists, the id =" + model.getId(),
                    "MODEL_IS_EXISTS");
        }
        model.setHistoryStatus(HistoryStatus.CREATING);
        this.modelDataHolder.add(model);
        fillTaskHistoryData(model);
        return model;
    }

    @TMSAllowedRoles({Role.ADMIN})
    public boolean saveNewItemList(final List<S> itemList) {
        if (Objects.isNull(itemList)) {
            throw new TMSException("Saving new item list is not possible, because the itemList is null!");
        }
        if (itemList.isEmpty()) {
            throw new TMSException("Saving new item list is not possible, because of itemList is empty!");
        }
        itemList.forEach(this::saveNewItem);
        return true;
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
