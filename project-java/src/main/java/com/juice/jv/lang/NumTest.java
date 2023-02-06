package com.juice.jv.lang;

public class NumTest {
    /**
     *有范围限定的整数存储及运算讨论
     *  Java中的整数类型有byte, short, int, long, 和char。
     *  其中byte, short, int, long分别以8-bit, 16-bit, 32-bit and 64-bit signed two's-complement(二进制补码) 存储整数值本身。
     *  char类型中存储的是16-bits unsigned integers(格式当然也是二进制补码)，其代表的意义是UTF-16的代码单元。
     *
     *第一: 以long类型为例，long类型的范围限定为[-2^63, 2^63-1]
     * 十进制 -2^63            -2^63 + 1           -2^63 + 2        ...    -1                 0                   1               ...   2^63-2            2^63-1
     * 原码  1,000 ,,, 0000   1,111 ,,, 1111      1,111 ,,, 1110   ...    1,000 ,,, 0001     0,000 ,,, 0000      0,000 ,,, 0001  ...   0,111 ,,, 1110    0,111 ,,, 1111
     * 补码  1,000 ,,, 0000   1,000 ,,, 0001      1,000 ,,, 0010   ...    1,111 ,,, 1111     0,000 ,,, 0000      0,000 ,,, 0001  ...   0,111 ,,, 1110    0,111 ,,, 1111
     *
     *第二: 加减法
     *  十进制: (2^63-1) + 1 = 2^63 (越界, 滚动到-2^63)
     *  补码:   0,111 ,,, 1111 + 0001 = 1,000 ,,, 0000 (越界是补码计算的必然结果)
     *
     *  十进制: (-2^63) - 1 = -2^63 - 1 (越界, 滚动到2^63-1)
     *  补码:   1,000 ,,, 0000 - 0001 = 0,111 ,,, 1111 (越界是补码计算的必然结果)
     *
     *  加减法: 表现为在数轴上的滚动(自动越界)
     *
     *第三: 减法转化为加法
     *  减去一个数 <==> 加上这个数的负数。存在一个例外情况: Number - Long.MIN_VALUE <=/=> Number + (-Long.MIN_VALUE), 因为 -Long.MIN_VALUE == Long.MIN_VALUE
     *
     *第四: -运算
     *  十进制: -(-2^63) = 2^63 (越界，滚动到-2^63)
     *  补码:  -x = 1,000 ,,, 0000 + Δ(0,111 ,,, 1111 - x) + 0001
     *
     *  注意: -0 == 0; -Long.MIN_VALUE == Long.MIN_VALUE
     *
     *  -运算的结果: 除了 0 和 Long.MIN_VALUE 的其他十进制数x，-x表现为x前添加一个负号。
     *
     *第五: 乘法
     *   补码: a*b，如果b为正数，结果为 a*b
     *            如果b为负数，结果为 (-a)*(-b)
     *      原理: 镜像法 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6
     *           1. a=3, b=2
     *           2. a=-3, b=2
     *           3. a=3, b=-2 ==> a=-3, b=2
     *           4. a=-3, b=-2 ==> a=3, b=2
     *   十进制乘法的运算: 镜像法
     *   note: Long.MIN_VALUE参与的乘法，只有两个结果: Long.MIN_VALUE 或 0
     *
     *   乘法的交换律，结合律，分解率仍然成立
     *
     *第六: 除法
     *    除数 / 被除数 = 商 ... 余
     *    1.数运算: |除数| 按 |被除数| 个数 分成了几组，不够一组的数量余下多少 (不会超界)
     *    2.符号运算，商的符号为 除数，被除数的符号运算
     *             余的符号和除数一致
     *    3. eg: -127/2 = -63 ... -1 , -127 每 2 个一组，分成-63组余-1 (另外，floorMod {@link Math#floorMod(int, int)})
     *
     *第七: 整数类型转换原理
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
     * 1. 小空间转大空间，补位，正数补0，负数补1
     * 2. 大空间转小空间，截断
     *
     *根据算法的规则，整数类型转换，只需看其范围，就能很快确定转换是否是可预料的正确的。
     *  Integal_Type common_convert(Integal_Type source_value, Class<Integal_Type> target_type):
     *      if source_value in range of target_type's valid range:
     *          return (target's Integal_Type) source_value
     *      else: throw unexpected
     *
     */



}
