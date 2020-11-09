package org.lastnpe.examples.limitations;

import org.eclipse.jdt.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ComputeIfAbsent {
    private Map<@NonNull Object, @NonNull Object> map = new HashMap<>();

    // helper function for mapping, return-value non-null
    @NonNull Object nonNullMappingFunction(Object key) {
        return new Object();
    }

    // helper function for mapping, return-value null
    @Nullable Object nullableMappingFunction(Object key) {
        return null;
    }

    // this should succeed (computeIfAbsent returns @NonNull if the return value of the mapping function is @NonNull)
    @NonNull Object mappingFunctionReturningNonNullAndMethodReturningNonNull() {
        Object key = new Object();
        return map.computeIfAbsent(key, this::nonNullMappingFunction);
    }

    // this should also succeed (@NonNull values are compatible with @Nullable)
    @Nullable Object mappingFunctionReturningNonNullAndMethodReturningNullable() {
        Object key = new Object();
        return map.computeIfAbsent(key, this::nonNullMappingFunction);
    }

    // this should fail (computeIfAbsent returns @Nullable if the return value of the mapping function is @Nullable)
    @NonNull Object mappingReturningNullableAndMethodReturningNonNull() {
        Object key = new Object();
        return map.computeIfAbsent(key, this::nullableMappingFunction);
    }

    // this should succeed
    @Nullable Object computeWithMappingFunctionNullableAndReturnValueNullable() {
        Object key = new Object();
        return map.computeIfAbsent(key, this::nullableMappingFunction);
    }

    // this should always fail (and does), null-mapping functions are not allowed
    Object mappingFunction() {
        Object key = new Object();
        return map.computeIfAbsent(key, null);
    }

    // possible annotations:

    // (TK;L1java/util/function/Function<-TK;+T0V;>;)T0V;
    // mapping function @NonNull, return-value of mapping function @Nullable and return value of method @Nullable
    //
    // [ERROR] C:\Users\Jan\Documents\Git\eclipse-null-eea-augments\examples\maven\limitations\src\main\java\org\lastnpe\examples\limitations\ComputeIfAbsent.java:[27]
    // [ERROR]         return map.computeIfAbsent(key, this::nonNullMappingFunction);
    // [ERROR]                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // [ERROR] Null type mismatch (type annotations): required '@NonNull Object' but this expression has type '@Nullable Object'
    //
    // this should not fail: the return value of the mapping function is @NonNull
    //
    // [ERROR] C:\Users\Jan\Documents\Git\eclipse-null-eea-augments\examples\maven\limitations\src\main\java\org\lastnpe\examples\limitations\ComputeIfAbsent.java:[39]
    // [ERROR]         return map.computeIfAbsent(key, this::nullableMappingFunction);
    // [ERROR]                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // [ERROR] Null type mismatch (type annotations): required '@NonNull Object' but this expression has type '@Nullable Object'
    //
    // this is ok to fail

    // (TK;L1java/util/function/Function<-TK;+TV;>;)TV;
    //
    // [ERROR] C:\Users\Jan\Documents\Git\eclipse-null-eea-augments\examples\maven\limitations\src\main\java\org\lastnpe\examples\limitations\ComputeIfAbsent.java:[39]
    // [ERROR]         return map.computeIfAbsent(key, this::nullableMappingFunction);
    // [ERROR]                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // [ERROR] Null type mismatch at method return type: Method descriptor Function<Object,Object>.apply(Object) promises '@NonNull Object' but referenced method provides '@Nullable Object'
    //
    // this should fail, but not because of the mapping functions return value but because of the return value of computeIfAbsent
    //
    // [ERROR] C:\Users\Jan\Documents\Git\eclipse-null-eea-augments\examples\maven\limitations\src\main\java\org\lastnpe\examples\limitations\ComputeIfAbsent.java:[45]
    // [ERROR]         return map.computeIfAbsent(key, this::nullableMappingFunction);
    // [ERROR]                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // [ERROR] Null type mismatch at method return type: Method descriptor Function<Object,Object>.apply(Object) promises '@NonNull Object' but referenced method provides '@Nullable Object'
    //
    // this should not fail because the return value of the method is @Nullable, so it's ok for computeIfAbsent to return null

    // (TK;L1java/util/function/Function<-TK;+T0V;>;)TV;
    //
    // this never fails, even if should because the mapping function returns null (and so does the method)

    // the problem is that we cannot annotate "infer the nullness of the methods return-value from the nullness of a parameter

    // side note: the same problem arises in Gson.fromJson which only returns null if the first parameter is null, so if that is guaranteed to be non-null, the return value is also non-null
}

