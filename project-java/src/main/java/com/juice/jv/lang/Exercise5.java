package com.juice.jv.lang;

public class Exercise5 {

    public static void main(String[] argv) {

        System.out.println(calGrownRabbit(-1, 1_000_000_000));
    }

    /**
     * 计算从起始年(include)到结束年(exclude)，rabbit 的数量。
     *
     * @param from 起始年份，include
     * @param to   结束年份，exclude
     * @return rabbit 的数量
     * @throws IllegalArgumentException 如果起始年份大于结束年份
     * @throws ArithmeticException 如果 rabbit 的数量超过 long 的范围
     */
    static long calGrownRabbit(long from, long to) {
        if(from > to) throw new IllegalArgumentException("起始年份大于结束年份");

        long totalDays = Math.multiplyExact(Math.subtractExact(to, from), 365);
        totalDays = Math.addExact(totalDays, countLeapYear(from, to));

        long incOfDay = 86400/7 - 86400/13 + 86400/45;
        long result = Math.multiplyExact(totalDays, incOfDay);

        result += (totalDays * (86400 % 7))/7 - (totalDays * (86400 % 13))/13 + (totalDays * (86400 % 45))/45;

        return result;
    }

    static long countLeapYear(long from, long to) { //[from, to), from <= to
        if(to < 0) {
            return countLeapYear(from) - countLeapYear(to);
        } else if(from < 0) {
            return countLeapYear(from) + countLeapYear(to);
        } else {
            return countLeapYear(to) - countLeapYear(from);
        }
    }
    static long countLeapYear(long year) {
        if(year >= 0) {
            if(year > Long.MAX_VALUE - 399) { //overflow
                long y4 = year / 4 + (year % 4 + 3) / 4;
                long y100 = year / 100 + (year % 100 + 99) / 100;
                long y400 = year / 400 + (year % 400 + 399) / 400;
                return y4 - y100 + y400;
            } else {
                return (year + 3) / 4 - (year + 99) / 100 + (year + 399) / 400;
            }
        } else {
            return year / -4 - year / -100 + year / -400;
        }
    }

}
