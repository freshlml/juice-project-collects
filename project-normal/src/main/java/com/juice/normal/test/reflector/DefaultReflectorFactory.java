package com.juice.normal.test.reflector;

public class DefaultReflectorFactory extends FlAbstractReflectorFactory {
    @Override
    public FlReflector findNew(Class<?> type) {
        return new FlReflector(type);
    }
}
