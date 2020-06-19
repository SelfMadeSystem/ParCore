package uwu.smsgamer.parcore.utils;

import java.security.*;

public class Sha1Obj<T> {
    private static MessageDigest md;
    private T value;
    private byte[] key;

    public Sha1Obj(T store, String info) {
        value = store;
        setKey(info);
    }

    private static MessageDigest getMD() {
        if (md != null) return md;
        try {
            return md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new Error("Unknown digest \"SHA-1\"!?!?");
    }

    private static byte[] digest(byte[] bytes) {
        return getMD().digest(bytes);
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte value : b) {
            result.append(Integer.toString((value & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public void setKey(String info) {
        byte[] bytes = info.getBytes();
        key = digest(bytes);
    }

    public boolean equals(String info) {
        return equals(info.getBytes());
    }

    public boolean equals(Sha1Obj<?> obj) {
        return equals(obj.key);
    }

    public boolean equals(byte[] bytes) {
        return key == digest(bytes);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value, String info) {
        setKey(info);
        this.value = value;
    }
}
