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
 *4. 固定位数的二进制小数(定点小数)
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
 *第二、ANSI/IEEE Standard 754-1985: single-precision 32-bit floating-point and double-precision 64-bit floating-point
 *    IEEE745规定了两种浮点数格式, 32位单精度浮点数，64位双精度浮点数。双精度浮点数的最小粒度更小，双精度浮点数的数轴包含单精度浮点数的数轴。
 *
 *         4bytes         31(1位)      30-----23(8位)      22----0(23位)
 *
 *                      S: 实数符号位    E: 阶码             M: 有效数位
 *
 *         8bytes         63(1位)      62-----52(11位)     51----0(52位)
 *
 *                      S: 实数符号位    E: 阶码             M: 有效数位
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
 *阶码E，按其作用分类，可分成如下几类:
 *  1. 阶码E=1111_1111, 用于标记无穷和NaN
 *  2. 阶码E∈(0000_0000, 1111_1111), 用于表示指数值, 指数值e = 阶码E - 偏置值
 *  3. 阶码E=0000_0000, 用于标记"非规格化小数", 指数值e = 1 - 偏置值 = 0000_0001 - 0111_1111 = 1000_0010, 指数e恒为-126(十进制)
 *
 *  8位阶码表，如下
 *    E取值       计算: 阶码E-偏置值                    意义
 *    1111_1111,                                  , 用于标记无穷和NaN
 *    1111_1110, 1111_1110 - 0111_1111 = 0111_1111, +127,指数e为+127(十进制数)
 *    ...
 *    1000_0000, 1000_0000 - 0111_1111 = 0000_0001, +1,指数e为+1(十进制数)
 *    0111_1111, 0111_1111 - 0111_1111 = 0000_0000, 0,指数e为+0(十进制数)
 *    0111_1110, 0111_1110 - 0111_1111 = 1111_1111, -1,指数e为-1(十进制数)
 *    ...
 *    0000_0001, 0000_0001 - 0111_1111 = 1000_0010, -126,指数e为-126(十进制数)
 *    0000_0000,                                  , 用于标记"非规格化小数"
 *
 *  注: 当使用阶码E表示指数值时，指数值的大小比较只需按阶码E的"最高有效位比较法"即可得出
 *
 *有效数位M
 *  对于"规格化小数"，m = 1.M(二进制)
 *  对于"非规格化小数"，m = 0.M(二进制)
 *
 *1. float value set
 * a. NaN。所谓NaN是指一些运算(如负数开方等数学上未定义的运算)未定义时运算部件统一返回的特殊值。
 *    NaN的存储值: S=0或1任意，E=1111_1111，M不全为0。此定义的S，M部分均是变量，在Java中，a single "canonical" NaN 的S=0，M=100,...,0000
 *
 * b. 负无穷
 *    负无穷的存储格式: S=1 && E=1111_1111 && M全为0
 * c. 正无穷
 *    正无穷的存储格式: S=0 && E=1111_1111 && M全为0
 *
 * d. "规格化小数", 阶码E∈(0000_0000, 1111_1111), 阶码E代表指数，指数e = E - 偏置值
 *    值范围:
 *      0 0000_0001 000,...,0000   ~   0 1111_1110 111,...,1111
 *      +1.000,...,0000 * 2^(-126) ~   +1.111,...,1111 * 2^127        注，指数-126，127是十进制数，表示小数点移动的位数
 *
 *      1 1111_1110 111,...,1111   ~   1 0000_0001 000,...,0000
 *      -1.111,...,1111 * 2^127    ~   -1.000,...,0000 * 2^(-126)
 *    最小粒度: 0.000,...,0001 * 2^(-126)
 *    最大粒度: 0.000,...,0001 * 2^127
 *
 * e. "非规格化小数", 阶码E恒取0000_0000，指数e = 1 - 偏置值 = -126, 指数e恒取-126
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
 *  最小粒度: 0.000,...,0001 * 2^(-126)
 *
 *3. 将十进制小数转化为"规格化小数"的二进制存储
 *   a. 确定S，0表示正数，1表示负数。
 *   b. 除去符号，将实数(整数部分和小数部分)化为二进制表示
 *   c. 小数点左移或者右移至第一个有效数字右边，从小数点右边第一位开始数出二十三位数字，找到舍入位，进行舍入后得到M
 *   d. 如果小数点左移指数取+N, 如果小数点右移指数取-N, 将指数以补码表示，加上偏置值0111_1111, 可得到E
 * 以11.9为例
 *   a. S=0
 *   b. 11.9化为二进制为 1011.111_0011_0011_0011_0011_0011_0011...
 *   c. 小数点左移3位，并从小数点右边第一位开始数出二十三位数字舍入后得到 1.011_1110_0110_0110_0110_0110
 *   d. E = 0000_0011 + 0111_1111 = 1000_0010
 * 11.9的存储值为
 *   0 1000_0010 011_1110_0110_0110_0110_0110
 *
 *4. 存储精度丢失与舍入
 *                                               |舍入
 *   11.9 化为二进制 1011.111_0011_0011_0011_0011_0|011_0011_0011_0011...
 *
 *   24位有效位后面的位舍入后，11.9的存储值为 0 1000_0010 011_111_0011_0011_0011_0011_0
 *
 *   如果按照11.9的存储值转化成十进制数，得到  1.011_111_0011_0011_0011_0011_0 * 2^3
 *                                   = 1011.111_0011_0011_0011_0011_0
 *                                   = 11.8999996185302734 (十进制)
 *
 *   a. 打印浮点数，使用PrintStream.printf("%.16f", float), 11.9 打印出 11.8999996185302734
 *   b. 注意System.out.print打印浮点数值的规则 {@link Float#toString()}
 *   c. 将浮点数的存储值完整输出: int i = Float.floatToIntBits(11.9f); Integer.toHexString(i);
 *
 *4.1 舍入规则
 *   按中点舍入：1. 第24位有效位为0，则如果第25位为0或者第25位为1且25位后面均为0，则从第25位起舍掉；如果第25位为1且25位后面不全为0，则从第25位起舍掉并 入1(按当前粒度)
 *            2. 第24位有效位为1，则如果第25位为0，则从第25位起舍掉；如果第25位为1，则从第25位起舍掉并 入1(按当前粒度)
 *   舍入时的边界判断: -∞，-0，+0，+∞
 *
 *4.2 具有与 1011.111_0011_0011_0011_0011_0 相同存储值的范围讨论 (按中点舍入而不是截断)
 *                                 |                                  |                                   |                                  |                                   |                                  |                                   |
 *   1011.111_0011_0011_0011_0010_1|,   1011.111_0011_0011_0011_0010_1|1,   1011.111_0011_0011_0011_0011_0|,   1011.111_0011_0011_0011_0011_0|1,   1011.111_0011_0011_0011_0011_1|,   1011.111_0011_0011_0011_0011_1|1,   1011.111_0011_0011_0011_0100_0|
 *   11.89999866485595703125,           11.899999141693115234375,           11.8999996185302734375,            11.900000095367431640625,           11.90000057220458984375,           11.900001049041748046875,           11.90000152587890625
 *                                               中点                                                                    中点                                                                    中点
 *   (11.8999996185302734375 + 11.90000057220458984375) / 2 = 11.900000095367431640625
 *   (11.89999866485595703125 + 11.8999996185302734375) / 2 = 11.899999141693115234375
 *
 *   可得 [11.899999141693115234375, 11.900000095367431640625] 范围内值与 1011.111_0011_0011_0011_0011_0 (11.8999996185302734) 有相同的存储值
 *
 *4.3 当粒度变大时，具有相同存储值的范围也将变大
 *                               |                               |                                |                                |                                 |
 *  1011_1011_1011_1011_1011.0101|, 1011_1011_1011_1011_1011.0101|1, 1011_1011_1011_1011_1011.0110|,  1011_1011_1011_1011_1011.0110|1,  1011_1011_1011_1011_1011.0111|
 *                                               中点                                                                 中点
 *  与 1011_1011_1011_1011_1011.0110 (768_955.375) 有相同存储值的范围为 [768_955.34375, 768_955.40625]
 *
 *
 *第四、浮点数的比较运算
 *  比较算法("最高有效位比较法"): 比较S，所有正数大于负数；若正负号相同，比较阶码E，对于正数，阶码大的大，对于负数，阶码大的小；若阶码E相同，比较尾数，对于正数，尾数大的大，对于负数，尾数大的小。
 *
 *  可见，浮点数大小比较只有一处与数学上的大小比较矛盾，即具有相同存储值范围内的所有小数，比较结果将相等且当粒度变大时，具有相同存储值的范围也将变大，体现为误差更大。
 *  所以，浮点数大小比较需打个补丁，用于处理相同存储值范围内所有小数被判断成相等的情况。
 *      1. == 运算: 如果 == 返回false，则返回false；如果 == 返回true，还需进一步验证是否真 相等
 *      2. != 运算: 如果 != 返回true，则返回true；如果 != 返回false，还需进一步验证是否真 不相等
 *      3. <、> 运算: 如果 <、> 返回true，则返回true；如果 <、> 返回false且 == 返回true时，还需进一步验证是否真 小于、大于；否者返回false
 *      4. <=、>= 运算: 如果 <、> 返回true，则返回true；如果 <、> 返回false且 == 返回true时，还需进一步验证是否真 小于等于、大于等于；否则返回false
 *
 *  如果关系运算时，其中一个数为整数，则整数先转换成浮点数存储，然后比较，比较结果符合上述逻辑
 *
 *
 *第五、浮点数的 +、-、*、/、% 运算
 *   1. 使用存储的值进行运算，而存储的值大部分都进行了舍入
 *   2. 进行 +、-、*、/、% 运算(浮点数数轴，overflow,underflow)，运算的结果也可能进行了舍入
 *   因此，运算得出的只能是一定粒度下的近似值
 *
 * 浮点数运算的平台相关性：float-extended-exponent? double-extended-exponent?
 *   运算的中间结果保存在寄存器中，如intel处理器使用80位的寄存器保存中间结果以保留更多的精度，不同处理器的寄存器长度不一样，
 *   这就导致不同处理器上相同浮点数的运算得到不同的结果。
 *   Java 虚拟机的最初规范规定所有的中间计算都必须进行截断。但截断计算不仅可能导致溢出，而且由于截断操作需要消耗时间，所以在计算速度上实际上要比精确计算慢。
 *   为此，Java 程序设计语言给予了改进。在默认情况下，虚拟机设计者允许对中间计算结果采用扩展的精度。
 *   但是， 对于使用 strictfp 关键字标记的方法必须使用严格的浮点计算来生成可再生的结果。
 *
 * 12.0f - 11.9f
 *      12.0f     未舍入          0 1000_0010 1.100_000_0000_0000_0000_0000_0
 *        -
 *      11.9f     舍入了          0 1000_0010 1.011_111_0011_0011_0011_0011_0
 *      --------------------------------------------------------------------
 *                结果未舍入       0 1000_0010 0.000_000_1100_1100_1100_1101_0
 *      结果转化为十进制: 0.000_000_1100_1100_1100_1101_0 * 2^3
 *                   = 0000.000_1100_1100_1100_1101_0
 *                   = 0.100_0003_8146_9726_5625
 * System.out.printf("%.19f", 12.0f - 11.9f);  //0.100_0003_8146_9726_5600
 *
 * 另: 使用 Decimal 进行运算:
 *    1. 运算数精确存储
 *    2. 加法，减法，乘法运算，结果可被完整精确的保存。除法运算如果能够除尽，结果可被精确保存，如果除不尽，需要指定舍入规则(在十进制数值层面)
 *    3. 等价代换
 *       1). 加法，减法，乘法运算，等价转换成立. 如 (a + b) * c == a*c + b*c
 *       2). 有除法运算参与的式子的等价代换，必须证明"先除"与"后除"是等价的(证明方法，如，按对应到数学中的式子反推)。如
 *           (a + b/3) * c == a*c + (b/3)*c.  (b/3)*c  不一定等于  (b*c)/3, 如果 b 能够除尽 3时，(b/3)*c == (b*c)/3
 *
 *第六、数字类型转换
 *
 *float <-> double, float类型转换成double,其存储值被完整保留；double类型转换成float, 按舍入规则舍入
 *         符号位  .          .                                 |舍入                                                                  .
 *  float     □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□|⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱...⿱
 *                                                                                                                   |舍入                      .
 *  double    □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□|⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱...⿱
 *
 *floating-point number to integral type:
 * 1. the floating-point number is converted either to a long if T is long, or to an int if T is byte、short、char、or int
 *    a: If the floating-point number is NaN, the result is an int or long 0.
 *    b: If floating-point number 的整数部分 is out of range[-2^32, 2^32-1]，the result is the smallest or largest value of int.
 *       If floating-point number 的整数部分 is out of range[-2^63, 2^63-1]，the result is the smallest or largest value of long.
 *    c: 否则，the floating-point value is rounded to an integral value
 * 2.
 *    a: If T is int or long, the result of the conversion is the result of the first step.
 *    b: If T is byte、short or char, the result of the conversion is the result of a narrowing conversion to type T of the result of the first step.
 *
 *
 *float -> byte模型图, float to int, int to byte
 *                   从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-128, 127]之间，即得到该整数值
 *         符号位  .          .   |末尾                  .        |舍入               .
 *  float     □     1□□□_□□□|□ □□□□_□□□□ □□□□_□□□□|⿱⿱⿱⿱_⿱⿱⿱⿱...⿱
 *  byte      □    □□□□_□□□|
 *byte -> float, 没有任何位丢失
 *注，double -> byte, byte->double与上述类似
 *
 *float -> short模型图, float to int, int to short
 *                    从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-32768, 32767]之间，即得到该整数值
 *         符号位  .          .                 |末尾    .        |舍入               .
 *  float     □     1□□□_□□□□ □□□□_□□□|□ □□□□_□□□□|⿱⿱⿱⿱_⿱⿱⿱⿱...⿱
 *  short     □    □□□□_□□□□ □□□□_□□□|
 *short -> float, 没有任何位丢失
 *注，double -> short, short->double与上述类似
 *
 *float -> int模型图, 从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-2^24, 2^24-1]之间，即得到该整数值；如果整数部分在 [-2^31, -2^24) 或 (2^24-1, 2^31-1]，整数部分精度丢失
 *                                                               否则，整数部分超过int范围，smallest or largest value
 *         符号位  .          .                                 |舍入       . |末尾          .
 *  float     □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□|⿱⿱⿱⿱_⿱⿱⿱|⿱ ⿱⿱⿱⿱...⿱
 *  int       □    □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□|
 *int -> float, 当数值 大于 2^24-1 或者小于 -2^24 时, 舍入造成数值精度丢失
 *
 *double -> int模型图, 从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-2^31, 2^31-1]之间，即得到该整数值；否则，smallest or largest value
 *         符号位  .          .                                              |末尾                  .                   |舍入       .              .
 *  double    □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□|□ □□□□_□□□□ □□□□_□□□□ □□□□_□|⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱...⿱
 *  int       □    □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□|
 *int -> double, 没有任何位丢失
 *
 *float -> long模型图, 从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-2^24, 2^24-1]之间，即得到该整数值；如果整数部分在 [-2^63, -2^24) 或 (2^24-1, 2^63-1]，整数部分精度丢失
 *                                                                否则，整数部分超过long范围，smallest or largest value
 *         符号位  .          .                                 |舍入       .                                                           |末尾             .
 *  float     □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□|⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱⿱⿱⿱_⿱⿱⿱|⿱ ⿱⿱⿱⿱_⿱...⿱
 *  long      □    □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□|
 *long -> float, 当long的数值 大于 2^24-1 或者小于 -2^24 时, 舍入造成数值精度丢失
 *
 *double -> long模型图, 从数值层面看的表现: 丢弃小数点和小数部分，保留整数部分。如果整数部分在[-2^54, 2^54-1]之间，即得到该整数值；即得到该整数值；如果整数部分在 [-2^63, -2^54) 或 (2^54-1, 2^63-1]，整数部分精度丢失
 *                                                                 否则，整数部分超过long范围，smallest or largest value
 *         符号位  .          .                                                                                       |舍入       .     |末尾      .
 *  double    □     1□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□|⿱⿱⿱⿱_⿱⿱⿱⿱ ⿱|⿱⿱⿱...⿱
 *  long      □    □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□□□□ □□□□_□ □□□ □□□□_□□□|
 *long -> double, 当long的数值 大于 2^54-1 或者小于 -2^54 时, 舍入造成数值精度丢失
 *
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

        //[11.899999141693115234375, 11.900000095367431640625]具有与 11.8999996185302734 相同的存储值
        f = 11.8999996185302734f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(f)));   //413e6666
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

        //粒度变大，具有相同存储值的范围变大：[768_955.34375, 768_955.40625] 与 768_955.375 有相同存储值
        float ff = 768_955.375f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(ff)));  //493bbbb6
        float ff1 = 768_955.34375f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(ff1))); //493bbbb6
        float ff2 = 768_955.40625f;
        System.out.println(Integer.toHexString(Float.floatToIntBits(ff2))); //493bbbb6

        //比较运算的矛盾
        System.out.println(ff1 == ff2); //true, 即 768_955.34375 == 768_955.40625

        //浮点数的打印, 打印出真实的存储值，注意System.out.print打印的值只经过人为规则处理后的
        float fff = 11.9f;
        System.out.printf("%.16f\n", fff);  //11.8999996185302730
        float fff1 = 11.8999996185302734f;
        System.out.printf("%.16f\n", fff1);   //11.8999996185302730, 精度不够啊

        //减法运算
        System.out.printf("%.19f\n", 12.0f - 11.9f);  //0.1000003814697265600

        //float -> byte: 丢弃小数，如果整数部分在[-128, 127]范围之外，将发生截断
        System.out.println((byte) 127.021931391f);  //127
        System.out.println((byte) -128.021931391f);  //-128
        System.out.println((byte) 129.021931391f);  //-127, 发生截断
        System.out.println((byte) -129.021931391f); //127, 发生截断

        //int -> float类型转换: 超过[-2^24, 2^24-1]范围，可能造成精度丢失
        System.out.printf("%.1f\n", (float) 16777215);   //-16777215.0
        System.out.printf("%.1f\n", (float) 16777216);   //16777216.0
        System.out.printf("%.1f\n", (float) 16777217);   //16777216.0

        System.out.printf("%.1f\n", (float) -16777216);  //-16777216.0
        System.out.printf("%.1f\n", (float) -16777217);  //-16777216.0


        //float -> double, 存储值完全保留
        //11.9f:            +1011.1110 0110_0110 0110_0110
        //4027ccccc0000000, +1011.1110 0110_0110 0110_0110 | 0000_0000 0000_0000 0000_0000 0000_0
        System.out.println((Long.toHexString(Double.doubleToLongBits(11.9f))));

        //double -> float, 舍入
        //11.9,     +1011.1110 0110_0110 0110_0110 | 0110_0110 0110_0110 0110_0110 0110_1
        //413e6666, +1011.1110 0110_0110 0110_0110
        System.out.println((Integer.toHexString(Float.floatToIntBits((float) 11.9))));


    }


}
