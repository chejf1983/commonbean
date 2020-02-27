/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.convert;

import java.util.Queue;

/**
 *
 * @author chejf
 */
public abstract class MyConvert {
    public static void main(String [] args) throws MyConvertException{
        float out = MyConvert.ByteArrayToFloat(new byte[]{(byte)0x42, (byte)0xBE,(byte)0x00,(byte)0x00}, 0);
        System.out.println(out);
    }

    static boolean CheckInputParameter(byte[] data, int pos, int len) throws MyConvertException {
        if (data == null) {
            throw new MyConvertException("Null");
        }
        if (pos > data.length) {
            throw new MyConvertException("pos > data.length");
        }
        if (pos + len > data.length) {
            throw new MyConvertException("pos + len > data.length");
        }
        return true;
    }

    public static String ByteArrayToString(byte[] data, int pos, int len) throws MyConvertException {
        CheckInputParameter(data, pos, len);
        if (data[pos] == 0x00) {
            return " ";
        }

        byte[] cpy = new byte[len + 1];
        cpy[len] = 0x00;
        System.arraycopy(data, pos, cpy, 0, len);

        for (int i = 0; i < cpy.length; i++) {
            if (cpy[i] == 0x00) {
                byte[] tmp = new byte[i];
                System.arraycopy(cpy, 0, tmp, 0, i);
                return new String(tmp);
            }
        }

        return " ";
    }

    public static byte[] StringToByte(String value, int len) throws MyConvertException {
        if (value == null) {
            throw new MyConvertException("Null");
        }

        char[] ctmp = value.toCharArray();
        byte[] tmp = new byte[len];

        for (int i = 0; i < ctmp.length; i++) {
            if (i < tmp.length) {
                tmp[i] = (byte) ctmp[i];
            }
        }
        return tmp;
    }

    public static int ByteArrayToInteger(byte[] data, int pos) throws MyConvertException {
        CheckInputParameter(data, pos, 4);

        return ((0xff & data[pos]) << 24)
                | ((0xff & data[pos + 1]) << 16)
                | ((0xff & data[pos + 2]) << 8)
                | (0xff & data[pos + 3]);
    }

    public static int ByteArrayToUShort(byte[] data, int pos) throws MyConvertException {
        CheckInputParameter(data, pos, 2);

        return ((0xff & data[pos]) << 8)
                | (0xff & data[pos + 1]);
    }

    public static double ByteArrayToDouble(byte[] data, int pos) throws MyConvertException {
        CheckInputParameter(data, pos, 8);

        long tmp = ((0xffL & data[pos + 0]) << 56)
                | ((0xffL & data[pos + 1]) << 48)
                | ((0xffL & data[pos + 2]) << 40)
                | ((0xffL & data[pos + 3]) << 32)
                | ((0xffL & data[pos + 4]) << 24)
                | ((0xffL & data[pos + 5]) << 16)
                | ((0xffL & data[pos + 6]) << 8)
                | (0xffL & data[pos + 7]);

        return Double.longBitsToDouble(tmp);
    }

    public static long ByteArrayToLong(byte[] data, int pos) throws MyConvertException {
        CheckInputParameter(data, pos, 8);

        long tmp = ((0xffL & data[pos + 0]) << 56)
                | ((0xffL & data[pos + 1]) << 48)
                | ((0xffL & data[pos + 2]) << 40)
                | ((0xffL & data[pos + 3]) << 32)
                | ((0xffL & data[pos + 4]) << 24)
                | ((0xffL & data[pos + 5]) << 16)
                | ((0xffL & data[pos + 6]) << 8)
                | (0xffL & data[pos + 7]);

        return tmp;
    }

    public static float ByteArrayToFloat(byte[] data, int pos) throws MyConvertException {
        CheckInputParameter(data, pos, 4);

        int tmp = ((0xff & data[pos]) << 24)
                | ((0xff & data[pos + 1]) << 16)
                | ((0xff & data[pos + 2]) << 8)
                | (0xff & data[pos + 3]);

        return Float.intBitsToFloat(tmp);
    }

    public static byte[] IntegerToByteArray(int value) throws MyConvertException {
        byte[] data = new byte[4];
        data[0] = (byte) (0xFF & (value >> 24));
        data[1] = (byte) (0xFF & (value >> 16));
        data[2] = (byte) (0xFF & (value >> 8));
        data[3] = (byte) (0xFF & value);

        return data;
    }

    public static byte[] UShortToByteArray(int value) throws MyConvertException {
        byte[] data = new byte[2];
        data[0] = (byte) (0xFF & (value >> 8));
        data[1] = (byte) (0xFF & value);
        return data;
    }

    public static byte[] DoubleToByteArray(double value) throws MyConvertException {
        byte[] data = new byte[8];
        long tmp = Double.doubleToLongBits(value);

        data[0] = (byte) (0xFFL & (tmp >> 56));
        data[1] = (byte) (0xFFL & (tmp >> 48));
        data[2] = (byte) (0xFFL & (tmp >> 40));
        data[3] = (byte) (0xFFL & (tmp >> 32));
        data[4] = (byte) (0xFFL & (tmp >> 24));
        data[5] = (byte) (0xFFL & (tmp >> 16));
        data[6] = (byte) (0xFFL & (tmp >> 8));
        data[7] = (byte) (0xFFL & tmp);
        return data;
    }

    public static byte[] FloatToByteArray(float value) throws MyConvertException {
        byte[] data = new byte[4];
        int tmp = Float.floatToRawIntBits(value);

        data[0] = (byte) (0xFFL & (tmp >> 24));
        data[1] = (byte) (0xFFL & (tmp >> 16));
        data[2] = (byte) (0xFFL & (tmp >> 8));
        data[3] = (byte) (0xFFL & (tmp));

        return data;
    }

    public static byte[] ByteListtobyteArray(Queue<Byte> in) {
        byte[] out = new byte[in.size()];
        Byte[] tmp = in.toArray(new Byte[0]);

        for (int i = 0; i < in.size(); i++) {
            out[i] = tmp[i].byteValue();
        }

        return out;
    }

    public static byte[] LongToByteArray(long tmp) throws MyConvertException {
        byte[] data = new byte[8];

        data[0] = (byte) (0xFFL & (tmp >> 56));
        data[1] = (byte) (0xFFL & (tmp >> 48));
        data[2] = (byte) (0xFFL & (tmp >> 40));
        data[3] = (byte) (0xFFL & (tmp >> 32));
        data[4] = (byte) (0xFFL & (tmp >> 24));
        data[5] = (byte) (0xFFL & (tmp >> 16));
        data[6] = (byte) (0xFFL & (tmp >> 8));
        data[7] = (byte) (0xFFL & tmp);
        return data;
    }
}
