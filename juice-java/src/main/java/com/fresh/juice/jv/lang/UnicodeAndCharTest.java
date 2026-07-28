package com.fresh.juice.jv.lang;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *第一: unicode
 *  unicode 字符集: 对世界上所有可能出现的字符，分配一个唯一的数字值表示;
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
 *第二: UTF编码
 *  UTF: unicode transformation format
 *    unicode码点的存储格式
 *    存储存的是二进制、网络传输中传递的是二进制
 *    unicode规定了字符的码点，码点使用utf编码转化为二进制，这样字符就可以用于存储和传输了
 *
 *  UTF-8
 *      [U+0, U+007F] 一个字节存储， (U+007F, U+07FF] 两个字节存储，(U+07FF, U+FFFF] 三个字节存储，(U+FFFF, U+10FFFF] 四个字节存储
 *
 *      [U+0, U+007F], [0000 0000, 0111 1111], 最多7个比特位，取码点存储在一个字节，最高位为0，兼容ASCII
 *
 *      (U+007F, U+07FF] [0000 0000 1000 0000, 0000 0111 1111 1111]，最多11个比特位，取码点存储在两个字节，110x xxxx 10xx xxxx，最高位为11表示使用两个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
 *
 *      (U+07FF, U+FFFF] [0000 0000 0000 1000 1111 1111, 0000 0000 1111 1111 1111 1111]，最多16个比特位，取码点存储在三个字节，1110 xxxx 10xx xxxx 10xx xxxx，最高位111表示用三个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
 *
 *      (U+FFFF, U+10FFFF] [0000 0000 0001 0000 0000 0000 0000, 0001 0000 1111 1111 1111 1111]，最多21个比特位，取码点存储在四个字节，1111 0xxx 10xx xxxx 10xx xxxx 10xx xxxx，最高位1111表示用四个字节存储,10表示是多字节中的一个字节，x保存具体的码点二进制值
 *
 *      多个字节时，每个字节以10开头、110开头、1110开头、1111开头，因此每个字节都比U+7f(127)大，而0-127为ASKII，这应该是有意为之
 *
 *  UTF-16
 *      [U+0, U+FFFF] 两个字节存储，(U+FFFF, U+10FFFF] 四个字节存储
 *
 *      [U+0000, U+D700] 和 [U+E000, U+FFFF]直接使用码点作为存储值。中间的[U+D800, U+DBFF]作为高位代理、[U+DC00, U+DFFF]作为低位代理
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
 *           当码点∈[U+0, U+FFFF]时，一个代码单元即可表示,代码单元存码点
 *           当码点∈(U+FFFF, U+10FFFF]时，要使用两个代码单元表示,其中第一个代码单元存高位代理值(根据码点计算出来的),第二个代码单元存低位代理值(根据码点计算出来的)
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
 * 小字节序: 如            0xff 0xfe 0x2d 0x4e
 *      小字节序开头两个字节0xff 0xfe,后续字节需要转换顺序: 0x4e 0x2d
 * 大字节序: 如            0xfe 0xff 0x4e 0x2d
 *      大字节序开头两个字节0xfe 0xff,后续字节无需转换顺序: 0x4e 0x2d
 *
 * UTF-16BE: UTF-16大字节序            0xfe 0xff
 * UTF-16LE: UTF-16小字节序            0xff 0xfe
 * UTF-32BE: UTF-32大字节序            0x00 0x00 0xfe 0xff
 * UTF-32LE: UTF-32小字节序            0xff 0xfe 0x00 0x00
 *
 * UTF-8理论上可以不用BOM，但是某些windows应用如notepad++,它支持给UTF-8加上BOM,excel中会根据字节序列的开头是不是0xef 0xbb 0xbf来判断是否是UTF-8字节序列
 *  UTF-8	  0xef 0xbb 0xbf
 *
 * java中，使用utf-8编码时，不添加bom
 *        使用utf-16时，默认使用大字节序并添加bom
 *        使用utf-16be时，使用大字节序但不添加bom
 *        使用utf-16le时，使用小字节序但不添加bom
 *
 *
 *第三: java中char类型
 *  char表示UTF-16中的一个代码单元，一个代码单元2个字节，所以说char两个字节也没毛病
 *      1、unicode字符的码点∈[U+0, U+FFFF]时
 *        一个char(一个代码单元)存储该unicode字符的码点
 *      2、unicode字符的码点∈(U+FFFF, U+10FFFF]时
 *        char[2](两个代码单元)分别存储高位代理、低位代理
 *
 *  一个char无法处理码点值大于U+FFFF的unicode字符：
 *     若数据库中存𝕆，相应entity用char c; 则c无法保存𝕆，
 *     应该使用String str; 而此时str中的char[]长度为2（高位代理和低位代理）
 *
 */
public class UnicodeAndCharTest {
    //java中，因为有jvm的存在，其字符的编译，运行过程，@link java/lang/encoding.uxf
    public static void main(String[] argv) {
        //11101010101000110
        //char使用两个字节存储，截断成1101010101000110，54598
        char c = (char) 120134;
        System.out.println(c);    //핆
        char d = 54598;
        System.out.println(d);   //핆
        System.out.println("--------------------------------1-------------------------------");


        //当char赋值为 >U+FFFF码点 的字符时
        //char t = '𝕆';   //报错: Too many characters in character literal
        String hello = "𝕆"; //使用String代替char来测试

        int codePointCount = hello.codePointCount(0, hello.length()); //码点数量: 1
        int length = hello.length();  //char数组长度: 2, char[0]存高位代理，char[1]存低位代理

        int codePoint = hello.codePointAt(0);
        System.out.println(codePoint);              //𝕆的码点: 120134, U+1D546, 按照UTF-16计算该码点的代理值, 高位代理: U+D835,低位代理: U+DD46

        System.out.println(Integer.toHexString(hello.charAt(0)));  //高位代理值，d835
        System.out.println(Integer.toHexString(hello.charAt(1)));  //低位代理值，dd46

        System.out.println("--------------------------------2-------------------------------");


////编码与解码（或，编码转换）
        //编码: 字符序列 -> 字节序列
        //解码: 字节序列 -> 字符序列

        //java 中编码:
        //  unicode 字符序列（String 或 char[]） -> 字节序列（byte[]）：String#getBytes("编码"): byte[]
        //java 中解码:
        //  字节序列（byte[]） -> unicode 字符序列（String 或 char[]）：new String(bys, "编码"): String
        //
        //编码转换:
        //  utf-16 字节序列（String 或 char[]） -> 字节序列（byte[]）：String#getBytes("编码"): byte[]
        //  字节序列（byte[]） -> utf-16 字节序列（String 或 char[]）：new String(bys, "编码"): String


        /*第一: 在"utf编码"中编码与解码
            1、utf-8、utf-16、utf-32使用的都是unicode字符的码点
            2、编码过程: 以utf-8为例 (真实代码并不一定这样写，但效果是一样的)
                根据char[]计算得出字符的码点(如果是代理需要处理代理), 根据码点按照utf-8规范转换成字节序列
            3、解码过程: 以utf-8为例
                取utf-8字节序列, 按照utf-8规范转换成码点序列, 每一个码点按照utf-16规范转化并存储到char[]中
         */
        String str = ", h 𝕆 中 😚 符 >";
        //编码: 将unicode字符序列按照"utf-8编码"转换成字节序列      值得注意的是，这里的"unicode字符序列"的说法是不准确的(在java中)，因为java中String使用char[]表示字符序列，每个char存unicode码点按照utf-16转化的值，不过这和char直接存储unicode字符的码点没有多大区别(至少是在"unicode字符序列这一说法上")
        byte[] bys = str.getBytes(StandardCharsets.UTF_8);
        for (byte by : bys) {
            //utf-8编码的字节序列前面并没有添加bom
            //','的utf-8编码:0x2c，' 'utf-8编码:0x20，'h'的utf-8编码:0x68，'𝕆'的utf-8编码:0xf09d9586，'中'的utf-8编码:0xe4b8ad，'😚'的utf-8编码:0xf09f989a，'符'的utf-8编码:0xe7aca6，'>'的utf-8编码: 0x3e
            //System.out.print(Integer.toHexString(by));
            //System.out.print("   ");
        }
        //解码: 将utf-8字节序列按照"utf-8编码"转换成unicode字符序列
        System.out.println(new String(bys, StandardCharsets.UTF_8));        //, h 𝕆 中 😚 符 >

        bys = str.getBytes(StandardCharsets.UTF_16);//默认使用大字节序
        for (byte by : bys) {
            //0xfe 0xff大字节序标记
            //','的utf-16be编码:0x002c，' 'utf-16be编码:0x0020，'h'的utf-16be编码:0x0068，'𝕆'的utf-16be编码:0xd835dd46，'中'的utf-16be编码:0x4e2d，'😚'的utf-16be编码:0xd83dde1a，'符'的utf-16be编码:0x7b26，'>'的utf-16be编码: 0x003e
            //System.out.print(Integer.toHexString(by));
            //System.out.print("   ");
        }
        System.out.println(new String(bys, StandardCharsets.UTF_16));        //, h 𝕆 中 😚 符 >

        bys = str.getBytes(StandardCharsets.UTF_16LE);//小字节序
        for (byte by : bys) {
            //使用utf-16le时无bom
            //','的utf-16le编码:0x2c00，' 'utf-16le编码:0x2000，'h'的utf-16le编码:0x6800，'𝕆'的utf-16le编码:0x35d846dd，'中'的utf-16le编码:0x2d4e，'😚'的utf-16le编码:0x3dd81ade，'符'的utf-16le编码:0x267b，'>'的utf-16le编码: 0x3e00
            //System.out.print(Integer.toHexString(by));
            //System.out.print("   ");
        }
        System.out.println(new String(bys, StandardCharsets.UTF_16LE));        //, h 𝕆 中 😚 符 >

        bys = str.getBytes(StandardCharsets.UTF_16BE);//大字节序
        for (byte by : bys) {
            //使用utf-16be时无bom
            //','的utf-16be编码:0x002c，' 'utf-16be编码:0x0020，'h'的utf-16be编码:0x0068，'𝕆'的utf-16be编码:0xd835dd46，'中'的utf-16be编码:0x4e2d，'😚'的utf-16be编码:0xd83dde1a，'符'的utf-16be编码:0x7b26，'>'的utf-16be编码: 0x003e
            //System.out.print(Integer.toHexString(by));
            //System.out.print("   ");
        }
        System.out.println(new String(bys, StandardCharsets.UTF_16BE));        //, h 𝕆 中 😚 符 >

        //第二: 其他编码规范中的编码与解码,eg: gbk、gb18030
        /* 1、字符在不同编码规范下的对比
        tool: https://www.qqxiuzi.cn/bianma/zifuji.php
        字符        ','            'h'             '𝕆'             '中'            '😚'           '符'          '>'
        GBK         2C            68              没有             D6D0            没有           B7FB          3E
        GB18030     2C            68              9433AA38        D6D0            95308132      B7FB          3E
        Unicode     0000002C      00000068        0001D546        00004E2D        0001F61A      00007B26      0000003E
        UTF-8       2C            68              F09D9586        E4B8AD          F09F989A      E7ACA6        3E
        UTF-16BE    002C          0068            D835DD46        4E2D            D83DDE1A      7B26          003E
        UTF-16LE    2C00          6800            35D846DD        2D4E            3DD81ADE      267B          3E00

        note1: 键盘上的英文字符，符号，数字，GBK、GB18030、unicode有相同的码点
        note2: 基本中文字符，GBK、GB18030有相同的码点，和unicode码点不同
        note3: 特殊字符如😚、𝕆，GBK没有、GB18030有码点，和unicode码点不同
         */
        /*2、unicode字符序列在"其他编码"(eg:"gbk编码"、"gb18030编码")中编码与解码
            1)、"其他编码"和unicode对相同字符不一定使用相同的码点
            2)、额外的字符映射: unicode字符(unicode码点)是否能够通过一些算法映射到"其他编码"中相同的字符(码点)上，
                如果能够映射，则按"其他编码"的规范执行编码得到字节序列，如果不能映射，则使用"其他编码"的替代字符执行编码得到字节序列
            3)、编码过程: 以gbk为例 (真实代码并不一定这样写，但效果是一样的)
                根据char[]计算得出字符的码点(如果是代理需要处理代理), 根据unicode码点映射到gbk，如果能够映射到相同的字符，取gbk码点，按gbk规范转换成字节序列
                如果没有映射，使用gbk中的'?'(3F)字符作为替代字符转换成字节序列
            4)、解码过程
                取gbk字节序列, 按照gbk规范转换成码点序列, 每一个码点映射到unicode, 如果能够映射到相同的字符，取unicode码点，按按UTF-16规范转化并存储到char[]中
            5)、如果在编码和解码过程中出现字符不能映射，字节原信息将丢失，就没办法转化回原来的字符了
         */
        bys = str.getBytes(Charset.forName("gbk"));
        for (byte by : bys) {
            //在gbk中, 0x2c对应','，0x20对应' '，0x68对应'h'，0x3F对应'?'，0xD6D0对应'中'，0xB7FB对应'符'，0x3E对应'>'
            //System.out.print(Integer.toHexString(by));
            //System.out.print("   ");
        }
        String gbkStr = new String(bys, Charset.forName("gbk"));  //gbk的字节序列按gbk规范转换成gbk中的码点(字符): 2c对应',' 20对应' '  68对应'h'  3F对应'?'  D6D0对应'中' B7FB对应'符' 3E对应'>'，得到", h ? 中 ? 符 >"
        //按gbk码点(字符)映射到unicode: unicode中, ','对应2C  ' '对应20  'h'对应68  '?'对应3F  '中'对应4E2D  '符'对应7B26  '>'对应3E
        //取对应字符的unicode码点，按UTF-16规范转化并存储到char[]中
        System.out.println(gbkStr);                                           //, h ? 中 ? 符 >

        bys = str.getBytes(Charset.forName("gb18030"));
        System.out.println(new String(bys, Charset.forName("gb18030")));       //, h 𝕆 中 😚 符 >

        bys = str.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(new String(bys, StandardCharsets.ISO_8859_1));    //, h ? ? ? ? >

        System.out.println("--------3-------");

        
    }

}
