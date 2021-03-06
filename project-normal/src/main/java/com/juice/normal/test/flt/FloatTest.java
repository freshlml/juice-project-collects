package com.juice.normal.test.flt;

public class FloatTest {

    public static void main(String argv[]) {

        /**
         *1、十进制整数和二进制数的转换
         * 11
         *  11/2=5   余   1
         *  5/2=2   余   1
         *  2/2=1   余   0
         *  1/2=0   余   1
         *  0结束   11二进制表示为(从下往上): 1011
         *这里提一点：只要遇到除以后的结果为0了就结束了，大家想一想，所有的整数除以2是不是一定能够最终得到0。
         * 换句话说，所有的整数转变为二进制数的算法会不会无限循环下去呢？绝对不会，整数永远可以用二进制精确表示 ，但小数就不一定了。
         *
         *2、十进制小数和二进制数的转换
         * 算法是乘以2直到没有了小数为止。举个例子，0.9表示成二进制数
         *   0.9*2=1.8   取整数部分  1
         *   0.8(1.8的小数部分)*2=1.6    取整数部分  1
         *   0.6*2=1.2   取整数部分  1
         *   0.2*2=0.4   取整数部分  0
         *   0.4*2=0.8   取整数部分  0
         *   0.8*2=1.6   取整数部分  1
         *   0.6*2=1.2   取整数部分  1  出现重复了，所以会无限计算下去
         *   .........      0.9二进制表示为(从上往下): 111'0011'0011'0011'0011'0011...
         *注意：上面的计算过程循环了，也就是说*2永远不可能消灭小数部分，这样算法将无限下去。很显然，小数的二进制表示有时候是不能精确的。
         * 其实道理很简单，十进制系统中能不能准确表示出1/3呢？同样二进制系统也无法准确表示1/10。这也就解释了为什么浮点型减法出现了"减不尽"的精度丢失问题。
         *
         *tool: https://tool.oschina.net/hexconvert/
         *
         *3、浮点数的存储
         *以float为例，float四个字节表示
         * float内存存储结构
         *
         *         4bytes         31(1位)      30(1位)      29----23(7)位    22----0(23位)
         *
         *                      实数符号位      指数符号位         指数位          有效数位
         *
         *         其中符号位0表示正，1表示负。有效位数有24位(1位整数和23位小数)
         *
         * 将十进制小数转化为二进制存储的步骤为：
         *
         *      （1）先将这个实数的绝对值化为二进制格式，注意实数的整数部分和小数部分的二进制方法在上面已经探讨过了
         *      （2）将这个二进制格式实数的小数点左移或右移n位，直到小数点移动到第一个有效数字的右边
         *      （3）从小数点右边第一位开始数出二十三位数字放入第22到第0位
         *      （4）如果实数是正的，则在第31位放入“0”，否则放入“1”
         *      （5）如果n 是左移得到的，说明指数是正的，第30位放入“1”。如果n是右移得到的或n=0，则第30位放入“0”
         *      （6）如果n 是左移得到的，则将n减去1后化为二进制，并在左边加“0”补足七位，放入第29到第23位。如果n是右移得到的或n=0，则将n化为二进制后在左边加“0”补足七位，再各位求反，再放入第29到第23位
         *
         *  举例说明: 11.9的内存存储格式
         *
         *        (1) 将11.9化为二进制后大约是 1011.111'0011'0011'0011'0011'0...
         *
         *        (2) 将小数点左移三位到第一个有效位右侧： 1.011'111'0011'0011'0011'0011'0 。 保证有效位数24位，右侧多余的截取
         *
         *        (3) 这已经有了二十四位有效数字，将最左边一位“1”去掉，得到  011'111'0011'0011'0011'0011'0  共23bit。将它放入float存储结构的第22到第0位
         *
         *        (4) 因为11.9是正数，因此在第31位实数符号位放入0
         *
         *        (5) 由于我们把小数点左移，因此在第30位指数符号位放入1
         *
         *        (6) 因为我们是把小数点左移3位，因此将3减去1得2，化为二进制，并补足7位得到0000010，放入第29到第23位
         *
         *    最后表示11.9为：  0 1 0000010 011'111'0011'0011'0011'0011'0
         *
         *  举例说明: 0.2356
         *         (1) 0.2356的二进制数  0.00111100010100000100100000
         *         (2) 小数点右移三位，1.11100010100000100100000
         *         (3) 从小数点右边数出二十三位有效数字放到第22到第0位
         *         (4) 因为0.2356是正数，因此在第31位实数符号位放入0
         *         (5) 由于我们把小数点右移，因此在第30位指数符号位放入0
         *         (6) 因为小数点被右移了3位，所以将3化为二进制，在左边补“0”补足七位，得到0000011，各位取反，得到1111100，放入第29到第23位
         *          0 0 1111100 11100010100000100100000
         *
         *  将一个内存存储的float二进制格式转化为十进制的步骤：
         *      （1）将第22位到第0位的二进制数写出来，在最左边补一位“1”，得到二十四位有效数字。将小数点点在最左边那个“1”的右边。
         *      （2）取出第29到第23位所表示的值n。当30位是“0”时将n各位求反。当30位是“1”时将n增1。
         *      （3）将小数点左移n位（当30位是“0”时）或右移n位（当30位是“1”时），得到一个二进制表示的实数。
         *      （4）将这个二进制实数化为十进制，并根据第31位是“0”还是“1”加上正号或负号即可。
         *
         *
         *所以,note1: 基本上很多浮点数在存储时都是不精确的
         *
         *4、浮点数的输出
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
         *所以,note2: 浮点数表S根据"规则"离散的表示了浮点数，如11.9: {11.9 - 1011.11100110011001100110}
         *           和11.9相近的浮点数转化为二进制后的值和11.9的二进制相等或者相近, 如果这样，那么这个相近的浮点数输出时就是11.9
         *
         *  3、则浮点数表的"规则"是什么? 浮点数的范围?
         *   todo
         *   浮点数精确位数内的能够精确表示,eg 1.23(两位)，121111.1(1.211111)
         *
         *
         *5、浮点数的计算
         *      12.0f 的内存存储格式为：     0 1 0000010 100 00000000000000000000
         *
         *      11.9f 的内存存储格式为:     0 1 0000010 011 11100110011001100110
         * 12.0f-11.9f   结果：           0 1 0000010 000 00011001100110011010 ===> 0.00011001100110011010
         *
         * System.out.println(0.10000038f);  //输出0.10000038, 1: 0.10000038存储(0.000110011001100110011111111)不精确，2: 输出精确{0.10000038 -- 0.000110011001100110011111111}
         * System.out.println(12.0f-11.9f);  //输出0.10000038, 浮点运算使用存储的二进制(一般都不是精确存储)
         *                                   //12.0f-11.9f的二进制结果为0.00011001100110011010       --->0.10000038146972656
         *                                   //0.10000038的二进制      0.000110011001100110011111111--->0.10000038
         *                                   //0.10000038146972656没有在浮点数表中，因此其找和他相近的值0.10000038
         *
         *所以,note3: 浮点数运算使用的是存储的二进制，计算结果一般都是不精确的
         *
         *note4: 浮点数和浮点数的运算一般应该用到数学计算的场景，业务计算中不能用浮点数
         *
         *note5: 相等性比较不是通过存储的二进制计算，而是看是否映射到浮点数表S中同一个浮点数
         *  System.out.println(11.8999994f == 11.9f);  # true
         *
         *
         */







    }


}
