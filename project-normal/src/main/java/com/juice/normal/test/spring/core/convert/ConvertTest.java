package com.juice.normal.test.spring.core.convert;

public class ConvertTest {


    public static void main(String argv[]) {

        Class<OpEnum> opClazz = OpEnum.class;
        System.out.println(opClazz.getSuperclass());
        FlConverter<String, OpEnum> con = getConverter(OpEnum.class);
        OpEnum op = con.convert("OP");
        System.out.println(op);

        OpEnum po = opClazz.getEnumConstants()[1];
        System.out.println(po);


    }

    public static <T extends Enum> FlConverter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum(targetType);
    }


    private static class StringToEnum<T extends Enum> implements FlConverter<String, T> {

        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
