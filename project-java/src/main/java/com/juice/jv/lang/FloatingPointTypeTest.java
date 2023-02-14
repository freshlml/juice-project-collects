package com.juice.jv.lang;


/**
 *第一、十进制数转换为二进制数
 * 1.算法
 *   整数部分，除2取余
 *   小数部分，乘2取整
 * 2.以 11.9 为例
 *   整数部分 11
 *     11/2=5   余   1
 *     5/2=2   余   1
 *     2/2=1   余   0
 *     1/2=0   余   1
 *     0结束   11二进制表示为(从下往上): 1011
 *   这里提一点：只要遇到除以后的结果为0了就结束了，所有的整数除以2是不是一定能够最终得到0。
 *      换句话说，所有的整数转变为二进制数的算法不会无限循环下去，整数永远可以用二进制精确表示 ，但小数就不一定了。
 *
 *   小数部分 0.9
 *     0.9*2=1.8   取整数部分  1
 *     0.8(1.8的小数部分)*2=1.6    取整数部分  1
 *     0.6*2=1.2   取整数部分  1
 *     0.2*2=0.4   取整数部分  0
 *     0.4*2=0.8   取整数部分  0
 *     0.8*2=1.6   取整数部分  1
 *     0.6*2=1.2   取整数部分  1  出现重复了，所以会无限计算下去
 *     .........   0.9二进制表示为(从上往下): 0.111_0011_0011_0011_0011_0011...
 *   注意：上面的计算过程循环了，也就是说*2永远不可能消灭小数部分，这样算法将无限下去。很显然，小数的二进制表示有时候是不能精确的(有限的位数限制下)。
 *     其实道理很简单，十进制系统中能不能准确表示出1/3呢？同样二进制系统也无法准确表示1/10。这也就解释了为什么浮点型减法出现了"减不尽"的精度丢失问题。
 * 3.要点说明
 *  a: 十进制小数转化为二进制数时，在有限的位数限制下，该二进制数的并不能精确的表示十进制小数。
 *  b: 数学中的小数有无限多个，固定大小的空间只能离散的存储若干。
 *
 *数字进制转换工具: https://tool.oschina.net/hexconvert/
 *
 *第二: 固定位数的二进制小数
 * 1.假设二进制小数固定4位
 * 0.0000, 0.0
 * 0.0001, 0.0625
 * 0.0010, 0.125
 * 0.0011, 0.1875
 * 0.0100, 0.25
 * 0.0101, 0.3125
 * 0.0110, 0.375
 * 0.0111, 0.4375
 * 0.1000, 0.5
 * 0.1001, 0.5625
 * 0.1010, 0.625
 * 0.1011, 0.6875
 * 0.1100, 0.75
 * 0.1101, 0.8125
 * 0.1110, 0.875
 * 0.1111, 0.9375
 * 1.0,    1.0
 * 每隔0.0625表示0.0 到 1.0之间的小数
 *
 *
 *第三、IEEE745 for the Storage specification of single-precision 32-bit floating-point
 *
 *         4bytes         31(1位)      30-----23(8位)      22----0(23位)
 *
 *                      S: 实数符号位    E: 阶码             M: 有效数位
 *
 *N = {S,E,M}, 逻辑上使用S,E,M表示数N。N的实际值 n = (-1)^s * m * 2^e，其中s, e, m分别为S, E, M的实际值
 *
 *实数符号位S: 表示实数的符号位，当 n>0 时，s=0, 当 n<0 时，s=1
 *
 *阶码E
 * 8位阶码E的取值及意义如下:
 *    E取值       计算: 阶码E-偏置值                    意义
 *    1111_1111, 1111_1111 - 0111_1111 = 1000_0000, -128,用于标记无穷和NaN
 *    1111_1110, 1111_1110 - 0111_1111 = 0111_1111, +127,指数e为+127(十进制数)
 *    ...
 *    1000_0000, 1000_0000 - 0111_1111 = 0000_0001, +1,指数e为+1(十进制数)
 *    0111_1111, 0111_1111 - 0111_1111 = 0000_0000, 0,指数e为+0(十进制数)
 *    0111_1110, 0111_1110 - 0111_1111 = 1111_1111, -1,指数e为-1(十进制数)
 *    ...
 *    0000_0001, 0000_0001 - 0111_1111 = 1000_0010, -126,指数e为-126(十进制数)
 *    0000_0000, 0000_0000 - 0111_1111 = 1000_0001, -127,用于标记"非规格化小数"
 *
 *  对于"规格化小数"，阶码E != 1111_1111 && 0000_0000，用于表示指数e = E - 偏置值。
 *  对于"非规格化小数"，E恒取0000_0000，指数e = 1-偏置值 = 0000_0001 - 0111_1111 = 1000_0010, -126,指数e恒为-126(十进制)
 *
 *有效数位M
 *  对于"规格化小数"，m = 1.M(二进制)
 *  对于"非规格化小数"，m = 0.M(二进制)
 *
 *1. float value set
 * a. NaN。所谓NaN是指一些运算(如除0或负数开方等)非法时运算部件统一返回的特殊值。
 *    NaN的存储格式: E=1111_1111 && M不全为0。a single "canonical" NaN 的M为 100,...,0000
 *
 * b. 负无穷
 *    负无穷的存储格式: S=1 && E=1111_1111 && M全为0
 * c. 正无穷
 *    正无穷的存储格式: S=0 && E=1111_1111 && M全为0
 *
 * d. "规格化小数", 阶码E代表指数，指数e = E - 偏置值
 *    "规格化小数"的格式: E != 1111_1111 && 0000_0000
 *    值范围:
 *      0 0000_0001 000,...,0000   ~   0 1111_1110 111,...,1111
 *      +1.000,...,0000 * 2^(-126) ~   +1.111,...,1111 * 2^127        注，指数-126，127是十进制数，表示小数点移动的位数
 *
 *      1 1111_1110 111,...,1111   ~   1 0000_0001 000,...,0000
 *      -1.111,...,1111 * 2^127    ~   -1.000,...,0000 * 2^(-126)
 *    最小粒度: 0.000,...,0001 * 2^(-126)
 *    最大粒度: 0.000,...,0001 * 2^127
 *
 * e. "非规格化小数", 阶码E恒取0000_0000，指数e = 1 - 偏置值 恒= -126
 *    "非规格化小数"的格式: E=0000_0000
 *    值范围:
 *      0 0000_0000 000,...,0000   ~   0 0000_0000 111,...,1111
 *      +0.000,...,0000 * 2^(-126) ~   +0.111,...,1111 * 2^(-126)   注，"非规格化小数"最大正数的next(加上一个粒度)为"规格化小数"的最小正数
 *
 *      1 0000_0000 111,...,1111   ~   1 0000_0000 000,...,0000
 *      -0.111,...,1111 * 2^(-126) ~   -0.0 * 2^(-126)              注，"非规格化小数"最小负数的prev(减去一个粒度)为"规格化小数"的最大负数
 *    粒度: 0.000,...,0001 * 2^(-126)
 *
 *2. 浮点数数轴
 *  负无穷 负的"规格化小数" 负的"非规格化小数" 正的"非规格化小数" 正的"规格化小数" 正无穷
 *  粒度: 0.000,...,0001 * 2^(-126)
 *
 *3. 将十进制小数转化为"规格化小数"的二进制存储
 *   a. 确定S，0表示正数，1表示负数。
 *   b. 除去符号，将实数(整数部分和小数部分)化为二进制表示
 *   c. 小数点左移或者右移至第一个有效数字右边，从小数点右边第一位开始数出二十三位数字，即得到M
 *   d. 如果小数点左移指数取+N, 如果小数点右移指数取-N, 将指数以补码表示，加上偏置值0111_1111, 可得到E
 * 以11.9为例
 *   a. S=0
 *   b. 11.9化为二进制为 1011.111_0011_0011_0011_0011_0011_0011...
 *   c. 小数点左移3位，并从小数点右边第一位开始数出二十三位数字得到 1.011_1110_0110_0110_0110_0110
 *   d. E = 0000_0011 + 0111_1111 = 1000_0010
 * 11.9的存储值为
 *   0 1000_0010 011_1110_0110_0110_0110_0110
 *
 *4. 存储精度丢失                                   |舍入
 *   11.9 化为二进制 1011.111_0011_0011_0011_0011_0|011_0011_0011_0011...
 *
 *   11.9的存储值为 0 1000_0010 011_111_0011_0011_0011_0011_0
 *   如果按照11.9的存储值转化成十进制数，得到  1.011_111_0011_0011_0011_0011_0 * 2^3
 *                                   = 1011.111_0011_0011_0011_0011_0
 *                                   = 11.8999996185302734 (十进制)
 *
 *4.1 具有与 1011.111_0011_0011_0011_0011_0 相同存储值的范围讨论 (按中点舍入而不是截断)
 *   1011.111_0011_0011_0011_0010_1,   1011.111_0011_0011_0011_0010_11,   1011.111_0011_0011_0011_0011_0,   1011.111_0011_0011_0011_0011_01,   1011.111_0011_0011_0011_0011_1,   1011.111_0011_0011_0011_0011_11,   1011.111_0011_0011_0011_0100_0
 *   11.89999866485595703125,          11.899999141693115234375,          11.8999996185302734375,           11.900000095367431640625,          11.90000057220458984375,          11.900001049041748046875,          11.90000152587890625
 *                                          中点                                                                        中点                                                                  中点
 *   (11.8999996185302734375 + 11.90000057220458984375) / 2 = 11.900000095367431640625
 *   (11.89999866485595703125 + 11.8999996185302734375) / 2 = 11.899999141693115234375
 *
 *   可得 [11.899999141693115234375, 11.900000095367431640625] 范围内值与 1011.111_0011_0011_0011_0011_0 有相同的存储值
 *
 *
 *第四、浮点数表？舍入？float-extended-exponent？
 *
 *
 *3、浮点数的输出 todo 有问题
 * 浮点数表S: Java中double类型的格式基本遵循IEEE 754标准，尽管数学意义上的小数是连续的，但double仅仅能表示其中的一些离散点，把这些离散点组成的集合记为S，S的大小还是有限的
 *         如果要保存的小数P刚好在集合S内，那么double类型就能精确的表示P，否则double类型只能从集合S中找一个与P最近的离散点P'代替P
 *
 * float f = 11.9f;
 * System.out.println(f); //输出11.9
 * 如上1-3所述，11.9在存储时是会丢失位数的，即不能精确存储。但是11.9精确的输出了?
 *  1、并不是将存储的值拿出来转化为十进制输出就完事了，如果是这样，1011.11100110011001100110 转化为十进制 11.899999618530273，那么输出应该是11.899999618530273
 *  2、浮点数表S中根据"规则"离散的表示了浮点数，例如11.9这个数被记录: {11.9 - 1011.11100110011001100110}，所以11.9在输出时就是输出11.9
 *  for eg:
 *      System.out.println(11.9f);        //输出11.9
 *      System.out.println(11.8999994f);  //输出11.9
 *    why 11.8999994 输出11.9?
 *     11.8999994  的二进制 1011.11100110011001100101 (保留24位有效数字位)
 *     11.9        的二进制 1011.11100110011001100110   "两者相差不大"
 *     浮点数表中没有11.8999994的表示，11.8999994找相近的11.9，则输出11.9
 *
 *
 *
 *5、浮点数的计算 todo
 *      12.0f 的内存存储格式为：     0 1 0000010 100 00000000000000000000
 *
 *      11.9f 的内存存储格式为:     0 1 0000010 011 11100110011001100110
 *      12.0f-11.9f   结果：      0 1 0000010 000 00011001100110011010 ===> 0.00011001100110011010
 *
 * System.out.println(0.10000038f);  //输出0.10000038, 1: 0.10000038存储(0.000110011001100110011111111)不精确，2: 输出精确{0.10000038 -- 0.000110011001100110011111111}
 * System.out.println(12.0f-11.9f);  //输出0.10000038, 浮点运算使用存储的二进制(一般都不是精确存储)
 *                                   //12.0f-11.9f的二进制结果为0.00011001100110011010       --->0.10000038146972656
 *                                   //0.10000038的二进制      0.000110011001100110011111111--->0.10000038
 *                                   //0.10000038146972656没有在浮点数表中，因此其找和他相近的值0.10000038
 *
 *
 */
public class FloatingPointTypeTest {

    public static void main(String argv[]) {

        //NaN的存储值验证，7fc00000
        System.out.println(Integer.toHexString(Float.floatToIntBits(Float.NaN)));
        //正无穷的存储值验证，7f800000
        System.out.println(Integer.toHexString(Float.floatToIntBits(Float.POSITIVE_INFINITY)));
        //负无穷的存储值验证，ff800000
        System.out.println(Integer.toHexString(Float.floatToIntBits(Float.NEGATIVE_INFINITY)));
        //11.9的存储值验证，0 1000_0010 011_1110_0110_0110_0110_0110
        float f = 11.9f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f))); //413e6666
        //非规格化数的存储值验证，0 0000_0000 111_1111_1111_1111_1111_1111
        f = 0x0.fffffeP-126f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f))); //007fffff

        //[11.899999141693115234375, 11.900000095367431640625]具有相同的存储值
        float f1 = 11.899999141693115234374999999999f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f1)));  //413e6665
        float f2 = 11.899999141693115234375f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f2)));  //413e6666
        float f3 = 11.900000095367431640625f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f3)));  //413e6666
        float f4 = 11.900000095367431640625000000001f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f4)));  //413e6667
        float f6 = 11.90000104904174804687499999999f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f6)));  //413e6667
        float f7 = 11.900001049041748046875f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f7)));  //413e6668


    }


}
