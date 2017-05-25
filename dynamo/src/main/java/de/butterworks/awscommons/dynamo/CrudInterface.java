package de.butterworks.awscommons.dynamo;

import java.util.List;
import java.util.Optional;

public interface CrudInterface<T extends Identifyable> {

    Optional<T> get(final String id);

    List<T> getAll();

    void add(final T item);

    void truncate();

    void update(final T item);
}
