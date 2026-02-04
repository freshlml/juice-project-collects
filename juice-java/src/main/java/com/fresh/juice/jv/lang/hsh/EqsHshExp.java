package com.fresh.juice.jv.lang.hsh;

import com.fresh.core.utils.ObjectUtils;

public class EqsHshExp {
    private final int code;
    private final boolean isPrimary;
    private final Integer wing;
    private final String name;
    private final Bean2Cool cool;
    private final int[] codes;
    private final Bean2Cool[] cools;

    public EqsHshExp(int code, boolean isPrimary, Integer wing, String name, Bean2Cool cool, int[] codes, Bean2Cool[] cools) {
        this.code = code;
        this.isPrimary = isPrimary;
        this.wing = wing;
        this.name = name;
        this.cool = cool;
        this.codes = codes;
        this.cools = cools;
    }

    /*
     * 1、== 运算符
     * 2、equals 方法，对象相等性测试的规则方法
     *    Object {
     *       public boolean equals(Object obj) {
     *          return (this == obj);  # 对象引用值比较(从实现上来看，比较变量的内存存储值，和primitive的==比较没有本质上的区别)
     *       }
     *    }
     */

    /*
     *equals性质:
     * 1. reflexive(自反性): x.equals(x) return true
     * 2. symmetric(对称性): x.equals(y) return true <==> y.equals(x) return true
     * 3. transitive(传递性): x.equals(y) return true; y.equals(x) return true ==> x.equals(z) return true
     * 4. consistent(一致性): 多次调用 x.equals(y) 不修改对象的equals比较中使用的信息 & 多线程之间的线程安全性
     * 5. null: x.equals(null) return false
     */
    @Override
    public boolean equals(Object o) {
        //1.直接比较引用值; for 性质1
        if(this == o) return true;

        //2.判空; for 性质5
        if(o == null) return false;

        //3.比较Class 或者 instanceof. 通常，不兼容的类型之间不具备比较性。
        if(this.getClass() != o.getClass()) return false;  //this 与 o 要有相同的 Class。this 与 o 的所属类为 EqsHshExp 或其子类。
        //if(!(o instanceof EqsHshExp)) return false;      //o 的所属类可为 EqsHshExp 或者其子类，this 的所属类亦可为 EqsHshExp 或者其子类，并且两者不一定有相同的所属类。
        //if(EqsHshExp.class == this.getClass() && EqsHshExp.class == o.getClass()) return false;  //此种写法，则较为严格...

        EqsHshExp that = (EqsHshExp) o;
        //4.比较字段值
        // 1).对于primitive type(byte, short, int, long, char, boolean, float, double)，通过 == 比较,比较存储的二进制是否相等
        // 2).对于reference type(ClassOrInterfaceType, Type Variable, Array Type), 调用ObjectUtils.objEquals方法
        boolean flag = this.code==that.code;        //int, 通过==比较
        flag &= this.isPrimary==that.isPrimary;     //boolean, 通过==比较
        flag &= ObjectUtils.objEquals(this.wing, that.wing);    //Integer
        flag &= ObjectUtils.objEquals(this.name, that.name);    //String
        flag &= ObjectUtils.objEquals(this.cool, that.cool);    //Bean2Cool
        flag &= ObjectUtils.objEquals(this.codes, that.codes);  //int[]
        flag &= ObjectUtils.objEquals(this.cools, that.cools);  //Bean2Cool[]

        //5.可调用基类的equals来重用代码
        //flag &= super.equals(o);
        return flag;
    }

    /*
     * non-duplicate性质: If two objects are equal according to the equals(Object) method,
     * then calling the hashCode() method on each of the two objects must produce the same integer result
     * hash碰撞: high-hash，more performance
     *
     * 计算方法: 取equals中用到的若干字段计算hashcode值并将这些值按照一定规则组合起来
     *         Object中hashCode默认实现满足: non-duplicate性质
     *
     * immutable保证: HashMap的Key的选择
     *        hashCode方法如果使用对象中某些字段并且这些字段的值可以被修改，就会导致hashCode的值改变
     *        所以Key一般选不可变类型如String、Integer
     *        自定义的类，如果不可变的，就会有上述潜在的隐患
     */
    @Override
    public int hashCode() {
        //1).primitive type(byte, short, int, long, char, boolean, float, double)，使用其包装类型的static hashCode方法
        //2).reference type(ClassOrInterfaceType, Type Variable, Array Type), 调用ObjectUtils.objHashCode方法
        int hashCode = Integer.hashCode(this.code);
        hashCode = 29*hashCode + ObjectUtils.objHashCode(name);
        hashCode = 29*hashCode + ObjectUtils.objHashCode(cool);
        hashCode = 29*hashCode + ObjectUtils.objHashCode(codes);
        hashCode = 29*hashCode + ObjectUtils.objHashCode(cools);
        //如果equals中使用了super,则使用super
        //hashCode = 29*hashCode + super.hashCode();
        return hashCode;
    }


    public static void main(String[] argv) {
        /* EqsHshExp.equals(EqsHshExp)
         * */
        EqsHshExp exp1 = new EqsHshExp(1, true,
                1, "name", new Bean2Cool(1), new int[]{1}, new Bean2Cool[]{new Bean2Cool(2)});

        EqsHshExp exp2 = new EqsHshExp(1, true,
                1, "name", new Bean2Cool(1), new int[]{1}, new Bean2Cool[]{new Bean2Cool(2)});
        System.out.println("exp1 == exp2: " + exp1.equals(exp2));

        /* EqsHshExp.equals(EqsHshExpSub)
         * */
        EqsHshExpSub sub1 = new EqsHshExpSub(2, 1, true,
                1, "name", new Bean2Cool(1), new int[]{1}, new Bean2Cool[]{new Bean2Cool(2)});
        System.out.println("exp1 == sub1: " + exp1.equals(sub1));

        System.out.println("exp1 hashCode: " + exp1.hashCode());

    }




}
