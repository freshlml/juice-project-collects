package com.juice.reactor.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TimeEat {

    public static void start() {
        //Date 与 LocalDateTime

        //创建Date的可用方法 new Date(long milliSeconds); 根据时间戳 创建
        Date date = new Date(System.currentTimeMillis());

        long milliSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        //OffsetDateTime odt = OffsetDateTime.now(ZoneId.systemDefault());
        //ZoneOffset defaultOffset = odt.getOffset();
        //long milliSeconds = localDateTime.toInstant(defaultOffset).toEpochMilli();
        //创建Date
        new Date(milliSeconds);


        //老代码中已有值Date转化为LocalDateTime
        Instant instant = date.toInstant();
        LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

    }

    public static void localDateTime() {
        //系统默认时区的当前时间, LocalDateTime本身并不存储时区信息，一旦构造，使用final int year等字段保存时间值
        LocalDateTime.now();

        //创建LocalDateTime using 2010-10-09 10:09:09
        LocalDateTime localDateTime = LocalDateTime.of(2010, 10, 9, 10, 9, 9);

        //时间戳: 绝对时空的产物。如:
        //自UTC时间 1970-01-01 00:00:00 -->  某一个UTC时间: 2021-07-10 04:16:24 经过的秒(/毫秒)
        //而+8时间  1970-01-01 08:00:00 -->  +8时间: 2021-07-10 12:16:24      经过的秒(/毫秒)  时间戳与时区无关，是一个固定的值; 时间戳转化为时间需要指定在哪一个时区
        //获取时间戳
        System.currentTimeMillis(); //获取时间戳
        localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()); //获取时间戳: 确保系统默认时区是+8
        System.out.println(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()); //默认时区的时间(LocalDateTime不存储时区信息)指定在默认时区，获取时间戳


        //通过秒 纳秒(相对值) 时区偏移 创建
        LocalDateTime.ofEpochSecond(1286590149, 0, ZoneOffset.of("+8"));

        //parse format
        LocalDateTime.parse("2010-10-09 10:09:09");
        LocalDateTime.parse("2010-10-09 10:09:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime.parse("2010-10-09 10:09:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


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

        System.out.println(ZoneId.systemDefault().getId());

    }

}
