package com.juice.jv.lang;

import java.util.HashMap;

/**
 *有范围限定的整数存储及运算讨论
 *  byte, short, int, long, 和char。
 *  其中 byte, short, int, long 分别以 8-bit, 16-bit, 32-bit and 64-bit signed two's-complement(二进制补码)存储整数值本身。
 *  char 类型中存储的是 16-bits unsigned integers(格式当然也是二进制补码)，其代表的意义是 UTF-16 的代码单元。
 *
 *第一: 以long类型为例，long类型的范围限定为[-2^63, 2^63-1]
 * 十进制 -2^63            -2^63 + 1           -2^63 + 2        ...    -1                 0                   1               ...   2^63-2            2^63-1
 * 原码  1,000 ,,, 0000   1,111 ,,, 1111      1,111 ,,, 1110   ...    1,000 ,,, 0001     0,000 ,,, 0000      0,000 ,,, 0001  ...   0,111 ,,, 1110    0,111 ,,, 1111
 * 补码  1,000 ,,, 0000   1,000 ,,, 0001      1,000 ,,, 0010   ...    1,111 ,,, 1111     0,000 ,,, 0000      0,000 ,,, 0001  ...   0,111 ,,, 1110    0,111 ,,, 1111
 *
 *第二: 加减法
 *  十进制: (2^63-1) + 1 = 2^63 (越界滚动, 滚动到-2^63)
 *  补码:   0,111 ,,, 1111 + 0001 = 1,000 ,,, 0000 (越界是补码计算的必然结果)
 *
 *  十进制: (-2^63) - 1 = -2^63 - 1 (越界滚动, 滚动到2^63-1)
 *  补码:   1,000 ,,, 0000 - 0001 = 0,111 ,,, 1111 (越界是补码计算的必然结果)
 *
 *  从数轴上看，"加减法"表现为在数轴上移动，当到达边界时，发生越界滚动
 *
 *第三: `-`运算
 *  十进制: -(2^63-1)          =  -2^63+1                   -0 == 0
 *  补码:  -(0,111 ,,, 1111)  ->  1,000 ,,, 0001  (按位取反，加一)
 *
 *  对于 Long.MIN_VALUE, -Long.MIN_VALUE == Long.MIN_VALUE
 *  十进制: -(-2^63)          =  -2^63
 *  补码:  1,000 ,,, 0000    ->  1,000 ,,, 0000   (按位取反，加一)
 *
 *第四: 减法转化为加法
 *  减去一个数 <==> 加上这个数的相反数:  a - b  <==>  a + (-b)              即使 b = Long.MIN_VALUE, -Long.MIN_VALUE == Long.MIN_VALUE
 *
 *
 *第五: 乘法
 *  1. 乘法运算: 数值运算，符号运算。
 *  2. 在数轴上理解乘法: 镜像法 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6
 *                        1. a=3,  b=2
 *                        2. a=-3, b=2
 *                        3. a=3,  b=-2 ==> a=-3, b=2
 *                        4. a=-3, b=-2 ==> a=3,  b=2
 *
 *  note: Long.MIN_VALUE 乘 一个整数, 只有两个结果: Long.MIN_VALUE 或 0
 *
 *第六: 除法, 取余
 *  1. div(/), mod(%) 运算法则:
 *      a ÷ b，b != 0，a ∈ 整数 ==> 将 a 展开成 n*b+c, n ∈ 整数，|c| < |b| && |n*b| <= |a| ==> (n*b + c) ÷ b ==> a div b=n, a mod b=c
 *      eg: -57  ÷  12 ==> [ (-4)*12 + (-9) ] ÷  12  ==>  div = -4, mod = -9            ；-57不能写成 (-5)*12 + 3
 *      eg:  57  ÷  12 ==> [ (4*12 + 9) ]     ÷  12  ==>  div =  4, mod =  9            ； 57不能写成 5*12 - 3
 *      eg:  57  ÷ -12 ==> [ (-4)*(-12) + 9 ] ÷  12  ==>  div = -4, mod =  9            ； 57不能写成 (-5)*(-12) - 3
 *      eg: -57  ÷ -12 ==> [ 4*(-12) + (-9) ] ÷ -12  ==>  div =  4, mod = -9            ；-57不能写成 5*(-12) + 3
 *
 *      例外: -Long.MIN_VALUE / -1   ==   -Long.MIN_VALUE
 *
 *  2. floordiv, floormod: 除数、被除数异号，mod 结果不等于0，并且与被除数异号时，产生一个借位。fresh/.../11_Month
 *  3. 在数轴上理解 div，mod 运算: |除数| 按 |被除数| 分成了几组，不够一组的数量余下多少 (div, mod 运算不会 overflow 或 underflow)
 *  4. 数学中 ÷ 与 整数 div(/) 的关系可以描述为: a ÷ b <==> a/b + (a%b) ÷ b
 *
 *第七: overflow 判断
 * 1. 将 "938" 转化为数字
 *   1). 最低有效位
 *       0  + 8*10^0 = 8
 *       8  + 3*10^1 = 38
 *       38 + 9*10^2 = 938
 *   2). 最高有效位
 *       0*10  + 9 = 9
 *       9*10  + 3 = 93
 *       93*10 + 8 = 938
 *       如上每一步的乘法运算，加法运算皆可能 overflow
 * 2. a>0, a*10 的 overflow 判断
 *   1). a*10 如果发生 overflow, 则将得到一个与 a*10 在数学上的运算结果不同的值。该值可能大于a, 小于a, 等于a
 *   2). 使用 a <= MAX / 10 来判断 a*10 是否 overflow，其中 MAX / 10 不会 overflow
 * 3. a>0, b>0, a+b 的 overflow 判断
 *   1). a+b 如果发生 overflow, 则将得到一个与 a+b 在数学上的运算结果不同的值。该值小于a
 *   2). 使用 a <= MAX - b 来判断 a+b 是否 overflow，其中 MAX - b 不会 overflow
 * 4. a>0, b>0
 *   1). a+b, may overflow
 *   2). a-b, no overflow
 *   3). (-a) - (-b), may underflow
 *   4). (-a) + b, no overflow
 *
 *一: 整数运算必须处理 overflow (如下所述只是一些常见的思考方法，具体 overflow 的处理应该结合代码逻辑灵活处理)
 *  1. 如果中间运算发生 overflow，并且最终结果也发生 overflow (或者 out of valid range)，则中间运算可抛出异常来标记该 overflow
 *  2. 将 overflow 公式"等价代换"成相对应的 no overflow 公式从而得到 no overflow 值，或者代换成可用 1 处理的公式
 *  3. 使用更长的整数类型或者 BigInteger 来消除 overflow
 *二: 使用位运算特别是移位运算优化运算效率
 *
 *第八: 等价代换
 * - `+`, `-`, `*` 运算等价代换成立. 如: (a - b) * c == a*c - b*c
 * - `/`, `%` 运算，根据他们的运算法则，可得若干等价代换式子: fresh/.../11_Month
 * - `/` 参与的运算，通常需要证明(证明方法，如，看式子两边是否丢失相同精度的值)等价代换是否成立，如 (a + b/2) * c == a*c + (b/2)*c，但 (b/2)*c  不一定等于  (b*c)/2
 *
 * 1. 如果等式两边均无 overflow，则两边有相同的正确的结果
 * 2. 如果等式一边 overflow，而另一边 no overflow，则可用 no overflow 的一边来得到正确的结果
 *
 *
 *第九: 整数类型转换原理
 *                                    -128    -127      ...        -1            0           1       ...        127
 * byte                               0x80    0x81      ...       0xff          0x00        0x01     ...        0x7f
 *
 *                                                                               0           1       ...        127        ...    32767   ...   65535
 * char                                                                        0x00_00    0x00_01    ...       0x00_7f     ...   0x7f_ff  ...  0xff_ff
 *
 *               -32768      ...     -128     -127      ...      -1              0          1        ...        127         ...   32767
 * short        0x80_00      ...   0xff_80   0xff_81    ...    0xff_ff         0x00_00    0x00_01    ...       0x00_7f      ...  0x7f_ff
 *
 *截断和补位算法:
 * 1. 小空间转大空间，补位，正数补0，负数补1。对于 char (unsigned 16-bit) 只有正数，即只补0
 * 2. 大空间转小空间，截断
 *
 *根据算法的规则，整数类型转换，只需看其范围，就能很快确定转换是否是可预料的正确的。
 *  Integral_Type common_convert(Integral_Type source_value, Class<Integral_Type> target_type):
 *      if source_value in range of target_type's valid range:
 *          return (target's Integral_Type) source_value
 *      else: throw unexpected
 *
 */
public class RangeLimitedIntegralTest {

    public static void main(String[] argv) {
        //(i * 52429) >>> (16+3) == i/10 验证

        for(int i=1; i<=65536; i++) {
            if((i * 52429) >>> (16+3) != i/10) System.out.println(i);
        }


        //0 ~ 146097
        HashMap<Integer, Integer> hsm = new HashMap<>();
        for(int rmd = (146097-1); rmd >= 0; rmd--) {
            int r = (400 * rmd) / 146097;
            hsm.put(r, rmd);
        }
        hsm.forEach((k, v) -> {
            System.out.print(k);
            System.out.println(" = " + "366*" + v/366 + "+" + v%366);
        });


    }

}
