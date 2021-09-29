package com.project.normal.test.chr;

import java.nio.charset.Charset;

public class CharTest {

    /**
     *第一: unicode码/unicode字符集
     *  unicode码的概念: 对世界上所有可能出现的字符，分配一个唯一的数字值表示;
     *                 unicode字符集只是规定字符的数字值，而没有规定数字值的存储格式
     *
     *  码点(code point): unicode字符集中某个字符对应的数字值,目前unicode的码点范围为 U+0000 ~ U+10FFFF，共1114112个码点,1 0000 1111 1111 1111 1111（21个比特位）
     *
     *  代码级别(code plane): U+0000 ~ U+10FFFF范围内的1114112个码点, 按每2^16(65536)个值分成一组，总共可以分成17份，每一份叫做一个代码级别
     *                      如第一个代码级别的码点范围: U+0000 ~ U+FFFF
     *      plane0      [U+0000,   U+FFFF]    BMP(basic multilingual plane)[基础多语言plane]
     *      plane1      [U+10000,  U+1FFFF]   SMP(supplementary multilingual plane)[补充多语言plane]
     *      plane2      [U+20000,  U+2FFFF]   SIP(supplementary ideographic plane)[补充表意语言plane]
     *      plane3      [U+30000,  U+3FFFF]   TIP(tertiary ideographic plane)[第三表意语言plane]
     *      plane4-13   [U+40000,  U+DFFFF]   预留
     *      plane14     [U+E0000,  U+EFFFF]   SSP(supplementary special-purpose plane)[补充特殊用途plane]
     *      plane15     [U+F0000,  U+FFFFF]   SPUA-A(supplementary private user area-a plane)[补充私有用途plane]
     *      plane16     [U+100000, U+10FFFF]  APUS-B(supplementary private user area-b plane)[补充私有用途plane]
     *
     *  问题: 为什么当前unicode码点的最大值为U+10FFFF???
     *       高位代理和低位代理能组合出1048576个数字 + 65536 = 1114112，即U+10FFFF
     *
     *  高位代理和低位代理:
     *      [U+10000, U+10FFFF]范围，共1,048,576个数字
     *      plane0的 [U+D800, U+DBFF]  作为高位代理, 这个范围共1024个数字
     *      plane0的 [U+DC00, U+DFFF]  作为低位代理, 这个范围共1024个数字
     *      高位代理、低位代理总共能够代理的数字个数: 1024*1024 = 2^20 = 1048576，恰好够表示[U+10000, U+10FFFF]范围
     *
     *
     *  块(block):
     *      C0 Controls and Basic Latin block:            [U+0000, U+007F],  即从ASCII继承过来的128个字符
     *      CJK Unified Ideographs block:                 [U+4E00, U+9FFC],  包含大部分的中日韩文字
     *      Halfwidth and Fullwidth Forms block:          [U+FF00, U+FFEF],  用于英文字母/数字/日文/个别符号等一些字符的全角-半角相互转换
     *      Miscellaneous Symbols and Pictographs block:  [U+1F300, U+1F5FF], emoji表情
     *      Supplemental Symbols and Pictographs block:   [U+1F900, U+1F9FF], emoji表情
     *
     *
     *
     *
     *第二: UTF编码
     *  UTF: unicode transformation format
     *    unicode码点的存储格式
     *    存储存的是二进制、网络传输中传递的是二进制
     *    unicode规定了字符的码点，码点使用utf编码转化为二进制，这样字符就可以用于存储和传输了
     *
     *  UTF-8
     *      [U+0, U+007F] 一个字节存储， (U+007F, U+07FF] 两个字节存储，(U+07FF, U+FFFF] 三个字节存储，(U+FFFF, U+10FFFF] 四个字节存储
     *
     *      [U+0, U+007F], [0000 0000, 0111 1111], 最多7个比特位，取码点值存储在一个字节，最高位为0，兼容ASCII
     *
     *      (U+007F, U+07FF] [0000 0000 1000 0000, 0000 0111 1111 1111]，最多11个比特位，取码点值存储在两个字节，110x xxxx 10xx xxxx，最高位为11表示使用两个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
     *
     *      (U+07FF, U+FFFF] [0000 0000 0000 1000 1111 1111, 0000 0000 1111 1111 1111 1111]，最多16个比特位，取码点值存储在三个字节，1110 xxxx 10xx xxxx 10xx xxxx，最高位111表示用三个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
     *
     *      (U+FFFF, U+10FFFF] [0000 0000 0001 0000 0000 0000 0000, 0001 0000 1111 1111 1111 1111]，最多21个比特位，取码点值存储在四个字节，1111 0xxx 10xx xxxx 10xx xxxx 10xx xxxx，最高位1111表示用四个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
     *
     *
     *  UTF-16
     *      [U+0, U+FFFF] 两个字节存储，(U+FFFF, U+10FFFF] 四个字节存储
     *
     *      [U+0000, U+D700] 和 [U+E000, U+FFFF]直接使用码点值作为存储值。中间的[U+D800, U+DBFF]作为高位代理、[U+DC00, U+DFFF]作为低位代理
     *      [U+10000, U+10FFFF], 使用高位代理和低位代理存储:
     *        1、令 y = x - U+10000,x∈[U+10000, U+10FFFF]，则y∈[U+0, U+FFFFF]
     *        2、y(max) = U+FFFFF，转化为二进制为: 1111 1111 1111 1111 1111，共计20个比特位
     *        3、将y的比特位取出低十位(不够,高位补0)，作为w2， w2∈[U+0, U+3FF]
     *           令 p = w2 + U+DC00, w2∈[U+0, U+3FF]，p∈[U+DC00, U+DFFF]
     *           p的范围恰好是低位代理的范围
     *        4、将y的比特位取出高十位(不够,高位补0)，作为w1， w1∈[U+0, U+3FF]
     *           令 q = w1 = U+D800, w2∈[U+0, U+3FF]，p∈[U+D800, U+DBFF]
     *           q的范围恰好是高位代理的范围
     *
     *       UTF-16中两个字节为一个代码单元(code unit)
     *           当码点∈[U+0, U+FFFF]时，一个代码单元即可表示,代码单元存码点值
     *           当码点∈(U+FFFF, U+10FFFF]时，要使用两个代码单元表示,其中第一个代码单元存高位代理值(根据码点值计算出来的),第二个代码单元存低位代理值(根据码点值计算出来的)
     *
     *     eg  x = U+1D546
     *       y = x - U+10000 = U+1D546 - U+10000 = U+D546, 1101 0101 0100 0110
     *       w2 = 01 0100 0110，p = w2 + U+DC00 = U+146 + U+DC00 = U+DD46
     *       w1 = 0000 1101 01，q = w1 + U+D800 = U+35 + U+D800 = U+D835
     *
     *tool: https://tool.oschina.net/hexconvert/
     *
     *  UTF-32
     *      [U+0, U+10FFFF] 四个字节定长存储
     *      空间浪费，在网络传输时效率低
     *
     * Byte order mark
     *  UTF-8	            EF BB BF       1110 1111 1011 1011 1111
     *  UTF-16 (BE)	        FE FF          1111 1110 1111 1111
     *  UTF-16 (LE)	        FF FE          1111 1111 1111 1110
     *  UTF-32 (BE)	        00 00 FE FF    0000 0000 0000 0000 1111 1110 1111 1111
     *  UTF-32 (LE)	        FF FE 00 00    1111 1111 1111 1110 0000 0000 0000 0000
     *
     *
     *第三: java中char类型
     *  char表示UTF-16中的一个代码单元
     *      当码点∈[U+0, U+FFFF]时，一个代码单元表示码点，一个char，char中存储码点，(int) char即可得到码点
     *      当码点∈(U+FFFF, U+10FFFF]时，两个代码单元分别表示高位代理，低位代理，两个char，分别存储位代理，低位代理，需要根据如上utf-16算法计算得出码点
     *
     *  char类型不建议使用
     *     eg: 数据库中存𝕆，如果entity用char c; 就乱了
     *                    应该使用String str; 但要注意此时str中的char[]长度为2，即高位代理和低位代理
     *
     */

    public static void main(String argv[]) throws Exception {
        //11101010101000110
        //char使用两个字节存储，截断成1101010101000110，54598
        char c = (char) 120134;
        System.out.println(c);    //핆
        char d = 54598;
        System.out.println(d);   //핆
        System.out.println("--------1-------");


        //当char赋值为 >U+FFFF码点 的字符时
        //char t = '𝕆';   //报错: Too many characters in character literal
        String hello = "𝕆"; //使用String代替char来测试

        int codePointCount = hello.codePointCount(0, hello.length()); //码点数量: 1
        int index = hello.offsetByCodePoints(0, 0);
        int codePoint = hello.codePointAt(index);
        System.out.println(codePoint);              //𝕆的码点值: 120134, U+1D546, 按照UTF-16计算他的高位代理: U+D835,低位代理: U+DD46
        char[] chars = Character.toChars(codePoint);
        System.out.println(new String(chars));      //输出𝕆

        System.out.println(hello.length());         //输出2，𝕆需要两个代码单位存储(一个char只能表示一个代码单元)，所以这里String中char[]数组的大小为2,char[0]存高位代理，char[1]存低位代理
        System.out.println(hello.charAt(0));        //输出?，落在代理区域
        System.out.println((int) hello.charAt(0));  //55349, U+D835,  是𝕆的高位代理值
        System.out.println(hello.charAt(1));        //输出?，落在代理区域
        System.out.println((int) hello.charAt(1));  //56646, U+DD46,  是𝕆的低位代理值
        System.out.println("--------2-------");



        //utf-8、utf-16、utf-32: 对unicode码点的不同存储格式实现，相互转化没有问题
        //                      并且就算不同编码规范的码点值不同，只要码点值能够很好的映射，也是可以实现转换而不乱码的，@see如下说明
        String str = ", h 𝕆 中 😚 符 >";  //char[], 每一个char表示UTF-16的代码单元
        byte[] bys = str.getBytes(Charset.forName("utf-8"));
        System.out.println(new String(bys, Charset.forName("utf-8")));        //, h 𝕆 中 😚 符 >
        bys = str.getBytes(Charset.forName("utf-16"));
        System.out.println(new String(bys, Charset.forName("utf-16")));        //, h 𝕆 中 😚 符 >
        bys = str.getBytes(Charset.forName("utf-32"));
        System.out.println(new String(bys, Charset.forName("utf-32")));        //, h 𝕆 中 😚 符 >



        /*
https://www.qqxiuzi.cn/bianma/zifuji.php
, h 𝕆 中 😚 符 > 的编码值比较
,
GBK         2C
GB18030     2C
Unicode     0000002C
UTF-8       2C
UTF-16BE    002C
UTF-16LE    2C00

h
GBK         68
GB18030     68
Unicode     00000068
UTF-8       68
UTF-16BE    0068
UTF-16LE    6800

𝕆
GBK         没有
GB18030     9433AA38
Unicode     0001D546
UTF-8       F09D9586
UTF-16BE    D835DD46
UTF-16LE    35D846DD

中
GBK         D6D0
GB18030     D6D0
Unicode     00004E2D
UTF-8       E4B8AD
UTF-16BE    4E2D
UTF-16LE    2D4E

😚
GBK         没有
GB18030     95308132
Unicode     0001F61A
UTF-8       F09F989A
UTF-16BE    D83DDE1A
UTF-16LE    3DD81ADE

符
GBK         B7FB
GB18030     B7FB
Unicode     00007B26
UTF-8       E7ACA6
UTF-16BE    7B26
UTF-16LE    267B

>
GBK         3E
GB18030     3E
Unicode     0000003E
UTF-8       3E
UTF-16BE    003E
UTF-16LE    3E00

note1: 键盘上的英文字符，符号，数字，GBK、GB18030、unicode有相同的码点值
note2: 基本中文字符，GBK、GB18030有相同的码点值，和unicode码点值不同
note3: 特殊字符如😚、𝕆，GBK没有、GB18030有码点值，和unicode码点值不同


编码转化过程: 1、转化: unicode码点对应的字符映射到其他"编码"对应的字符，如果能够映射，按其他"编码"的规范得到编码值，如果没有映射，将使用其他"编码"的替代字符代替
           2、转回: 其他"编码"的字节流按其他"编码"找到字符，使用字符映射到unicode字符，如果能够映射，将unicode字符按照UTF-16规范转化成字节存到char，如果没有映射，将使用替代字符代替
编码转换原则: unicode码点对应的字符通过某个算法可以映射到其他"编码"对应的字符，则unicode码点可以转化成其他"编码"的字节流(通过str.getBytes("其他编码"))，其他"编码"的字节流也可以转化回unicode码点(new String(bys, "其他编码"))
           如果存在某些字符不能映射到其他"编码"，那么转化成其他"编码"后的字节流就会修改原来的信息，这些字符将不能转化回去
编码转换的核心: 字符映射
         */

        bys = str.getBytes(Charset.forName("gbk"));   //按gbk编码: 字符映射到gbk，取gbk中对应字符的编码值，如果没有映射，计为'?'(3F)
        for(byte by : bys) {
            System.out.print(Integer.toHexString(by));   //2c   20   68   20   3f   20   ffffffd6   ffffffd0   20   3f   20   ffffffb7   fffffffb   20   3e
            System.out.print("   "); //在gbk中, 2c对应',' 20对应' '  68对应'h'  3F对应'?'  D6D0对应'中' B7FB对应'符' 3E对应'>'
            //此时的bys中，不能映射的字符实际存的3F，这样原信息丢失了没办法转回去了
        }
        String gbkStr = new String(bys, Charset.forName("gbk"));  //gbk的字节流按gbk解码成gbk中的字符: 2c对应',' 20对应' '  68对应'h'  3F对应'?'  D6D0对应'中' B7FB对应'符' 3E对应'>'，得到", h ? 中 ? 符 >"
                                                                  //按字符映射到unicode: unicode中, ','对应2C  ' '对应20  'h'对应68  '?'对应3F  '中'对应4E2D  '符'对应7B26  '>'对应3E
                                                                  //取对应字符的unicode码点值，将unicode码点值按UTF-16规则转化
        System.out.println(gbkStr);                                           //, h ? 中 ? 符 >


        bys = str.getBytes(Charset.forName("gb18030"));
        System.out.println(new String(bys, Charset.forName("gb18030")));       //, h 𝕆 中 😚 符 >


        bys = str.getBytes(Charset.forName("iso-8859-1"));
        System.out.println(new String(bys, Charset.forName("iso-8859-1")));    //, h ? ? ? ? >

        System.out.println("--------3-------");

    }



}
