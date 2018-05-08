package cn.chenanduo.util;

import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by chen on 2017
 */

public class Util {
    /**
     * @param b byte[] byte[] b = { 0x12, 0x34, 0x56 };
     * @return String 123456
     */
    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase() + " ";
        }
        return ret;
    }

    /**
     * 用于数据转换 不是偶数在前面补0 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" -->
     * byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        if (src.length() % 2 == 1) {
            src = "0" + src;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = unionBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /*
    用于crc16效验  可能效验后长度为1  导致越界  不足补0x00
     */
    public static byte[] hexString2Byte(String src) {
        if (src.length() % 2 == 1) {
            src = "0" + src;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = unionBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        if (ret.length != 2) {
            byte[] bytes = new byte[2];
            bytes[0] = ret[0];
            bytes[1] = 0x00;
            return bytes;
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte unionBytes(byte src0, byte src1) {
        byte b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        b0 = (byte) (b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (b0 ^ _b1);
        return ret;
    }

    public static String Bytes2HexString_noblack(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /*
    将ascii码转换为字符串
    * 将16进制数字解码成字符串,适用于所有字符（包括中文）
    */
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    /*
    字符转转ascii码
    * 将字符串编码成16进制数字,适用于所有字符（包括中文）
    * ZJTT-03-L000 -->5A 4A 54 54 2D 30 33 2D 4C 30 30 30
    */
    private static String hexString = "0123456789ABCDEF";
    public static String encode(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }


    /**
     * 生成随机数
     *
     * @return
     */
    public static byte[] getRandomByte(int random) {
        byte[] bytes = new byte[random];
        Random ran = new Random();
        ran.nextBytes(bytes);
        return bytes;
    }


    /**
     * 获取16进制的时间表示
     *
     * @return
     */
    public static byte[] getTimeHexString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        // 20151007205012
        String time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String s = time.substring(0, 2);
        if (s.length() % 2 == 1) {
            s = "0" + s;
        }
        String yearString2 = time.substring(2, 4);
        if (yearString2.length() % 2 == 1) {
            yearString2 = "0" + yearString2;
        }

        String month = time.substring(4, 6);
        if (month.length() % 2 == 1) {
            month = "0" + month;
        }
        String day = time.substring(6, 8);
        if (day.length() % 2 == 1) {
            day = "0" + day;
        }
        String hour = time
                .substring(8, 10);
        if (hour.length() % 2 == 1) {
            hour = "0" + hour;
        }
        String minute = time.substring(10,
                12);
        if (minute.length() % 2 == 1) {
            minute = "0" + minute;
        }
        String second = time.substring(12);
        if (second.length() % 2 == 1) {
            second = "0" + second;
        }
        String timString = s + yearString2 + month + day + hour + minute + second;

        return HexString2Bytes(timString);
    }

    /**
     * 16进制时间显示
     *
     * @param time 时间参数  但是是没有补0的
     * @return newTime 时分秒 补零的 不需要判断
     */
    public static byte[] timeHexString(String time, String newTime) {
        String[] split = time.split("-");
        if (split[1].length() % 2 == 1) {
            split[1] = "0" + split[1];
        }
        if (split[2].length() % 2 == 1) {
            split[2] = "0" + split[2];
        }
        time = split[0] + split[1] + split[2] + newTime;
        //20170508 185424
        String yearString1 = time.substring(0, 2);
        if (yearString1.length() % 2 == 1) {
            yearString1 = "0" + yearString1;
        }
        String yearString2 = time.substring(2, 4);
        if (yearString2.length() % 2 == 1) {
            yearString2 = "0" + yearString2;
        }

        String month = time.substring(4, 6);
        if (month.length() % 2 == 1) {
            month = "0" + month;
        }
        String day = time.substring(6, 8);
        if (day.length() % 2 == 1) {
            day = "0" + day;
        }
        String hour = time
                .substring(8, 10);
        if (hour.length() % 2 == 1) {
            hour = "0" + hour;
        }
        String minute = time.substring(10,
                12);
        if (minute.length() % 2 == 1) {
            minute = "0" + minute;
        }
        String second = time.substring(12);
        if (second.length() % 2 == 1) {
            second = "0" + second;
        }
        String timString = yearString1 + yearString2 + month + day + hour + minute + second;
        return HexString2Bytes(timString);
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 二进制字符串转byte
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    public static  boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
}
