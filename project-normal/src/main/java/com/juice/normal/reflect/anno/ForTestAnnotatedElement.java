package com.juice.normal.reflect.anno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ForTestAnnotatedElement {

    public static void main(String argv[]) {
        testParameterAnnotations();


    }

    @InAnno //not captured
    private static class Base {}
    private static class Bean extends Base {}
    //private void m(@PoAnnotation(name="a") @PoAnnotation(name="b") Bean t) {} Container annotation '@PosAnnotation' is not applicable to parameter
    private void m(@DemoAnno @PoAnnotation(name="a") Bean t) {}

    private static void testParameterAnnotations() {
        //java.lang.reflect.Executable#getParameterAnnotations
        Method[] mds = ForTestAnnotatedElement.class.getDeclaredMethods();
        Method method = null;
        for(int i=0; i<mds.length; i++) {
            if(mds[i].getName() == "m") {
                method = mds[i];
                break;
            }
        }
        if(method != null) {
            Annotation[][] pannos = method.getParameterAnnotations();

            System.out.println(pannos);
        }
    }

}
