package de.butterworks.awscommons.dynamo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudInterface<T extends Identifyable> {

    Optional<T> get(final UUID id);

    List<T> getAll();

    void add(final T item);

    void truncate();

    void update(final T item);
}
