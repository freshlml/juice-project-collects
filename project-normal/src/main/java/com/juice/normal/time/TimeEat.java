package com.juice.normal.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TimeEat {

    /**
     * LocalDateTime
     * ZoneId: 时区ID; ZoneOffset继承ZoneId: 使用时区偏移表示时区
     * Instance: 时间戳
     */
    public static void localDateTime() {
        //系统默认时区的当前时间, LocalDateTime本身并不存储时区信息，一旦构造，使用final int year等字段保存时间值
        LocalDateTime.now();
        //+8区的当前时间
        LocalDateTime.now(ZoneOffset.of("+8"));
        //UTC时间(0区的当前时间)
        LocalDateTime.now(ZoneOffset.of("Z"));


        //时间戳: 绝对时空的产物。如:
        //自UTC时间 1970-01-01 00:00:00 -->  某一个UTC时间: 2021-07-10 04:16:24 经过的秒(/毫秒)
        //而+8时间  1970-01-01 08:00:00 -->  +8时间: 2021-07-10 12:16:24      经过的秒(/毫秒)
        //时间戳与时区无关，是一个固定的值; 时间戳转化为时间需要指定在哪一个时区
        System.out.println(System.currentTimeMillis()); //获取时间戳
        System.out.println(LocalDateTime.now().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
        System.out.println(LocalDateTime.now(ZoneOffset.of("+8")).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        System.out.println(LocalDateTime.now(ZoneOffset.of("Z")).toInstant(ZoneOffset.of("Z")).toEpochMilli());

        //根据时间戳，时区 创建
        long milliSeconds = LocalDateTime.now(ZoneOffset.of("+8")).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneOffset.of("+8"));

        //创建LocalDateTime using 2010-10-09 10:09:09
        LocalDateTime.of(2010, 10, 9, 10, 9, 9);
        //通过秒, 纳秒(相对值), 时区offset 创建
        LocalDateTime.ofEpochSecond(1286590149, 0, ZoneOffset.of("+8"));

        //parse, format
        LocalDateTime.parse("2010-10-09 10:09:09");
        LocalDateTime.parse("2010-10-09 10:09:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime.parse("2010-10-09 10:09:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime.parse("2010-10-09 10:09:09").format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        /*
        //字段读取get

        //字段修改with, 注意final
        LocalDate date1 = LocalDate.of(2014, 3, 18); //2014-03-18
        LocalDate date2 = date1.withYear(2011);  //2011-03-18

        //字段plus minus, 注意final
        LocalDate date3 = date2.plus(6, ChronoUnit.MONTHS);
        */
        LocalDateTime withDate = LocalDateTime.of(2010, 10, 9, 10, 9, 9);
        LocalDateTime withDate1 = withDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime withDate2 = withDate.with(temporal -> {
            DayOfWeek dow =
                    DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });

    }

    public static void date() {
        //可用的Date构造器
        //new Date()
        //new Date(long milliSeconds)
        Date date = new Date(System.currentTimeMillis()); //使用Date存储时间戳

        //老代码中某些方法可能仍然接受一个Date参数
        long milliSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        milliSeconds = LocalDateTime.now(ZoneOffset.of("+8")).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        new Date(milliSeconds);
        

        //老代码中已有值的Date转化为LocalDateTime
        Instant instant = date.toInstant();  //时间戳转储到Instant
        LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());

    }


    public void interval() {
        //Duration Period

        /*Duration d1 = Duration.between(time1, time2);
        Duration d2 = Duration.between(dateTime1, dateTime2);
        Duration d3 = Duration.between(instant1, instant2);
        Period d4 = Period.between(date1, date2);

        Duration threeMinutes = Duration.ofMinutes(3);
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);*/

    }


    public static void main(String argv[]) {



    }

}
