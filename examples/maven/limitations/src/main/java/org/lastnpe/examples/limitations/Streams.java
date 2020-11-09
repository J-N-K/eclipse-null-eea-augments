package org.lastnpe.examples.limitations;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@NonNullByDefault
public class Streams {
    List<@Nullable Object> nullableList = new ArrayList<>();
    List<Object> nonNullList = new ArrayList<>();

    List<@Nullable Object> nullableReturnList = new ArrayList<>();
    List<Object> nonNullReturnList = new ArrayList<>();

    // helper function for mapping, return-value non-null
    Object nonNullMappingFunction(@Nullable Object key) {
        return new Object();
    }

    // helper function for mapping, return-value null
    @Nullable Object nullableMappingFunction(@Nullable Object key) {
        return null;
    }

    void nullableConsumer(@Nullable Object object) {

    }
    void nonNullConsumer(Object object) {

    }

    void bothListsNullable() {
        // all should pass
        nullableList.stream().forEach(this::nullableConsumer);
        nullableList.stream().map(this::nonNullMappingFunction).forEach(this::nullableConsumer);
        nullableList.stream().map(this::nullableMappingFunction).forEach(this::nullableConsumer);
        nonNullList.stream().forEach(this::nullableConsumer);
        nonNullList.stream().map(this::nonNullMappingFunction).forEach(this::nullableConsumer);
        nonNullList.stream().map(this::nullableMappingFunction).forEach(this::nullableConsumer);
    }

    void returnListNonNull() {
        // should fail
        nullableList.stream().forEach(this::nonNullConsumer);
        // should pass
        nullableList.stream().map(this::nonNullMappingFunction).forEach(this::nonNullConsumer);
        // should fail
        nullableList.stream().map(this::nullableMappingFunction).forEach(this::nonNullConsumer);
        // should pass
        nonNullList.stream().forEach(this::nonNullConsumer);
        // should pass
        nonNullList.stream().map(this::nonNullMappingFunction).forEach(this::nonNullConsumer);
        // should fail
        nonNullList.stream().map(this::nullableMappingFunction).forEach(this::nonNullConsumer);
    }

    void limitation() {
        // should pass
        nonNullList.stream().map(this::nullableMappingFunction).filter(Objects::nonNull).forEach(this::nonNullConsumer);

        // but fails:
        //
        // [ERROR] C:\Users\Jan\Documents\Git\eclipse-null-eea-augments\examples\maven\limitations\src\main\java\org\lastnpe\examples\limitations\Streams.java:[63]
        // [ERROR]         nonNullList.stream().map(this::nullableMappingFunction).filter(Objects::nonNull).forEach(this::nonNullConsumer);
        // [ERROR]                                                                                                  ^^^^^^^^^^^^^^^^^^^^^
        // [ERROR] Null type mismatch at parameter 1: required '@NonNull Object' but provided '@Nullable Object' via method descriptor Consumer<Object>.accept(Object)
    }

}

