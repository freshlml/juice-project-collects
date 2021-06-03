package com.project.normal.reflector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class FlAbstractReflectorFactory implements FlReflectorFactory, FlCacheableReflectorFactory {
    private boolean classCacheEnabled = true;
    private final ConcurrentMap<Class<?>, FlReflector> reflectorMap = new ConcurrentHashMap<>();
    @Override
    public boolean getClassCacheEnabled() {
        return classCacheEnabled;
    }
    @Override
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    @Override
    public FlReflector findForClass(Class<?> type) {
        if (classCacheEnabled) {
            return reflectorMap.computeIfAbsent(type, FlReflector::new);
        } else {
            return findNew(type);
        }
    }

    protected abstract FlReflector findNew(Class<?> type);


}
