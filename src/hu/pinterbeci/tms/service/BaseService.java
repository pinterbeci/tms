package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.model.BaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseService<S extends BaseModel> {

    private final List<S> modelDataHolder;

    public BaseService() {
        this.modelDataHolder = new ArrayList<>();
    }

    public Optional<S> findById(final String id) {
        return Optional.of(
                modelDataHolder.stream()
                        .filter(savedModel -> Objects.equals(savedModel.getId(), id))
                        .findFirst()
                        //todo
                        // releváns exception
                        .orElseThrow(RuntimeException::new)
        );
    }

    public S create(final S newModel) {
        if (Objects.isNull(newModel)) {
            //todo
            // releváns exception
            //   NPE
            throw new RuntimeException();
        }

        if (Objects.nonNull(newModel.getId())) {
            findById(newModel.getId()).ifPresent(model -> {
                //todo
                // releváns exception
                // entity already saved
                throw new RuntimeException();
            });
        }
        return saveNewItem(newModel);
    }

    public S update(final S model, final String id) {
        final S savedModel = findById(id).orElseThrow(RuntimeException::new);

        //reflekciót használva, osztalyra, metodusra, mezo-re hivatkozhatok futasidoben, dinamikusan
        try {
            for (final Field field : model.getClass().getDeclaredFields()) {
                // Allow private field access
                field.setAccessible(true);
                // Get the value from the provided model
                final Object value = field.get(model);

                // Update-elem azon mezok erteket, melyek nem null-ok
                if (Objects.nonNull(value)) {
                    field.set(savedModel, value);
                }
            }
            //visszaadom az update-elt modelt
            return savedModel;
        } catch (IllegalAccessException e) {
            //todo
            // releváns exception
            throw new RuntimeException("Error while updating fields", e);
        }
    }

    public void delete(final String id) {
        findById(id).ifPresent(model -> model.setDeleted(true));
    }

    public List<S> getAll() {
        return this.modelDataHolder;
    }


    private S saveNewItem(final S model) {
        if (Objects.nonNull(model.getId())) {
            //todo
            // id nélkül nem hozható létre
            // ahol kapcsolatok állnak fenn, ott meg kell vizsgálni a kapcsolandó model, létre van e már hozva
            throw new RuntimeException();
        }
        this.modelDataHolder.add(model);
        return model;
    }
}
