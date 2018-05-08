package cn.chenanduo;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
                                //       f000c0e0-0451-4000-b000-000000000000
        String uuid1= "read-write-notify-00002902-0000-1000-8000-00805f9b34fb";//长度35
        String uuid2= "read-00002902-0000-1000-8000-00805f9b34fb";
        String uuid3= "write no response-00002902-0000-1000-8000-00805f9b34fb";
        //从该索引开始        从该索引结束  fromindex该搜索从指定位置开始
        String substring1 = uuid1.substring(uuid1.length()-35);
        String substring2 = uuid2.substring(uuid2.length()-35);
        String substring3 = uuid3.substring(uuid3.length()-35);
        System.out.println("substring：" + substring1);
        System.out.println("substring：" + substring2);
        System.out.println("substring：" + substring3);
    }
}