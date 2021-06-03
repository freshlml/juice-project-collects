import org.junit.Test;

import java.util.Date;

/**
 * Unit Test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {

        //byte short int long float double boolean String Object
        Byte b = 1;
        int i = 1;
        long l = 1931312312312312L;
        float f = 12.34f;
        double d = 12.34;
        String s = "123";
        Date dt = new Date();

        System.out.println(convert2Long(b));
        System.out.println(convert2Long(i));
        System.out.println(convert2Long(l));
        System.out.println(convert2Long(f));
        System.out.println(convert2Long(d));
        System.out.println(convert2Long(s));
        System.out.println(convert2Long(dt));

    }

    /**
     *
     * @param o
     * @throw NumberFormatException
     * @return
     */
    public Long convert2Long(Object o) {
        if(o == null) throw new NullPointerException("参数不能是null");
        if(o instanceof Number) return ((Number) o).longValue();
        return Long.parseLong(String.valueOf(o));
    }

}
