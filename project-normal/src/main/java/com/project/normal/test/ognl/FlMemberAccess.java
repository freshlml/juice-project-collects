package com.project.normal.test.ognl;

import ognl.MemberAccess;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Map;

public class FlMemberAccess implements MemberAccess {
    @Override
    public Object setup(Map context, Object root, Member member, String property) {
        Object result = null;
        if (isAccessible(context, root, member, property)) {
            AccessibleObject accessible = (AccessibleObject) member;
            if (!accessible.isAccessible()) {
                result = Boolean.FALSE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }
    @Override
    public void restore(Map context, Object root, Member member, String property, Object state) {

    }
    @Override
    public boolean isAccessible(Map context, Object root, Member member, String property) {
        return false;
    }
}
