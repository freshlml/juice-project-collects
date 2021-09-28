package com.project.normal.test.reflect;

import com.project.normal.enums.ScanTypeEnum;
import com.project.normal.test.reflector.FlReflectorFactory;
import com.project.normal.test.xml.GenericSAXParser;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClazzTest {


    public static void main(String argv[]) throws Exception {

        //declared class, enum, interface, annotation, array, primitive
        Class<GenericSAXParser> declaredClassClazz = GenericSAXParser.class;
        Class<ScanTypeEnum> enumClazz = ScanTypeEnum.class;
        Class<FlReflectorFactory> interfaceClazz = FlReflectorFactory.class;
        Class<PostConstruct> annotationClazz = PostConstruct.class;
        Class<int[]> intArrayClazz = int[].class;
        Class<GenericSAXParser[]> classArrayClazz = GenericSAXParser[].class;
        Class<Boolean> primitiveSeqClazz = Boolean.class;
        Class<Boolean> primitiveClazz = boolean.class;
        Class<Void> voidClass = void.class; //void 也是一种primitive

        Constructor<Book2> con1 = Book2.class.getConstructor(new Class<?>[]{Object.class});
        Constructor<Book2> con2 = Book2.class.getConstructor(new Class<?>[]{String.class});
        Constructor<Book2> con3 = Book2.class.getConstructor(new Class<?>[]{int[].class});
        Constructor<Book2> con4 = Book2.class.getConstructor(new Class<?>[]{Object[].class});
        Constructor<Book2> con5 = Book2.class.getConstructor(new Class<?>[]{Book.class});
        Constructor<Book2> con6 = Book2.class.getConstructor(new Class<?>[]{List[].class});
        Constructor<Book2> con7 = Book2.class.getConstructor(new Class<?>[]{Number.class});


        Constructor<?>[] ctor = Yui.class.getDeclaredConstructors();
        Arrays.stream(ctor).forEach(tor -> System.out.println(tor.isSynthetic()));

        System.out.println("1");



        //forName
        //Class<?> clazz = Class.forName("com.freshjuice.fl.xml.GenericSAXParser");
        //Class<GenericSAXParser> clazz2 = (Class<GenericSAXParser>) Class.forName("com.freshjuice.fl.xml.GenericSAXParser");

        //isInstance
        //System.out.println(Object.class.isInstance(GenericSAXParser.ofDTD(true)));
        //System.out.println(classArrayClazz.isInstance(new GenericSAXParser[10]));
        //System.out.println(Object[].class.isInstance(new GenericSAXParser[10]));
        //System.out.println(int.class.isInstance(1));

        //isAssignableFrom
        //System.out.println(Object[].class.isAssignableFrom(GenericSAXParser[].class));

        //is*
        //Class<Uiop.InnerOp> innerClazz = Uiop.InnerOp.class;
        //System.out.println(innerClazz.isMemberClass());
        //Class<Uiop.InnerStaticOp> innerStaticClazz = Uiop.InnerStaticOp.class;
        //System.out.println(innerStaticClazz.isMemberClass());

        //getName getSimpleName getTypeName getCanonicalName
        //System.out.println(classArrayClazz.getName());
        //System.out.println(interfaceClazz.getSimpleName());
        //System.out.println(classArrayClazz.getTypeName());
        //System.out.println(classArrayClazz.getCanonicalName());


        //getEnumConstants
        //ScanTypeEnum[] result = enumClazz.getEnumConstants();
        //System.out.println(result);

        //getClasses
        //Class<?>[] result = declaredClassClazz.getClasses();
        //System.out.println(result);

        //getSupperClass
        //System.out.println(GenericXpathXmlParser.class.getSuperclass());

        //getInterfaces
        //Class<?>[] result = classArrayClazz.getInterfaces();
        //System.out.println(result);

        //getSigners
        //Object[] result = declaredClassClazz.getSigners();
        //System.out.println(result);

        //annotation
        /*PoAnnotation r1 = ReflectClazz.class.getAnnotation(PoAnnotation.class);
        System.out.println(r1);
        PosAnnotation r2 = ReflectClazz.class.getAnnotation(PosAnnotation.class);
        System.out.println(r2);
        PoAnnotation[] r3 = ReflectClazz.class.getAnnotationsByType(PoAnnotation.class);
        System.out.println(r3);

        System.out.println(ReflectClazz.class.isAnnotationPresent(PoAnnotation.class));
        System.out.println(ReflectClazz.class.isAnnotationPresent(PosAnnotation.class));

        ScanTypeEnum sl = ScanTypeEnum.valueOf("SYSTEM");
        sl = ScanTypeEnum.valueOf(ScanTypeEnum.class, "SYSTEM");
        System.out.println(sl);*/


        /*Class<Book> bookClazz = (Class<Book>) Class.forName("com.freshjuice.fl.reflect.Book");
        System.out.println(bookClazz);
        TypeVariable<Class<Book>>[] typeVariables = bookClazz.getTypeParameters();

        //没法通过Class获取Book中的String
        Book<String> bookString = new Book<>();
        Class<? extends Book> bookStringClazz = bookString.getClass();
        TypeVariable<? extends Class<? extends Book>>[] typeVariables2 = bookStringClazz.getTypeParameters();
*/

    }

    private static class Yui {
        //public <T> Yui(T t) {}
    }

    public static List<Method> findAllInstanceMethodOnInterfaces(Class<?> clazz) {
        List<Method> result = new ArrayList<>();
        findAllInstanceMethodOnInterfaces(clazz, result);
        return result;
    }
    private static List<Method> findAllInstanceMethodOnInterfaces(Class<?> clazz, List<Method> result) {
        if(clazz.isInterface()) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isAbstract(method.getModifiers())) {
                    result.add(method);
                }
            }
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for(Class<?> inter : interfaces) {
            findAllInstanceMethodOnInterfaces(inter, result);
        }
        return result;
    }

    private static class Op implements AllInter2 {

        @Override
        public void inter2() {

        }

        @Override
        public void inter1() {

        }

        @Override
        public void interDefault() {

        }

        @Override
        public void inter() {

        }
    }


}
