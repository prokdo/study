package ru.prokdo.model.database.access;

public final class AccessKey {
    private boolean readBit;
    private boolean writeBit;
    private boolean delegateBit;

    public AccessKey(int accessKeyInt) {
        if (accessKeyInt < 0 || accessKeyInt > 7) throw new IllegalArgumentException("Access key out of bounds [0, 7]");

        String accessKeyBinStr = null;
        switch (accessKeyInt) {
            case 0 -> accessKeyBinStr = "000";
            case 1 -> accessKeyBinStr = "001";
            case 2 -> accessKeyBinStr = "010";
            case 3 -> accessKeyBinStr = "011";
            case 4 -> accessKeyBinStr = "100";
            case 5 -> accessKeyBinStr = "101";
            case 6 -> accessKeyBinStr = "110";
            case 7 -> accessKeyBinStr = "111";
        }

        this.readBit = accessKeyBinStr.charAt(0) == '1';
        this.writeBit = accessKeyBinStr.charAt(1) == '1';
        this.delegateBit = accessKeyBinStr.charAt(2) == '1';
    }

    public AccessKey(String accessKeyBinStr) {
        if (accessKeyBinStr.length() != 3) throw new IllegalArgumentException("Access key out of bounds [0, 7]");

        this.readBit = accessKeyBinStr.charAt(0) == '1';
        this.writeBit = accessKeyBinStr.charAt(1) == '1';
        this.delegateBit = accessKeyBinStr.charAt(2) == '1';
    }

    public boolean getReadBit() {
        return this.readBit;
    }

    public boolean getWriteBit() {
        return this.writeBit;
    }

    public boolean getDelegateBit() {
        return this.delegateBit;
    }

    public void setReadBit(boolean readBit) {
        this.readBit = readBit;
    }

    public void setWriteBit(boolean writeBit) {
        this.writeBit = writeBit;
    }

    public void setDelegateBit(boolean delegateBit) {
        this.delegateBit = delegateBit;
    }

    public String toBinStr() {
        StringBuilder result = new StringBuilder();

        if (this.readBit) result.append('1'); else result.append('0');
        if (this.writeBit) result.append('1'); else result.append('0');
        if (this.delegateBit) result.append('1'); else result.append('0'); 

        return result.toString();
    }

    public int toInt() {
        return Integer.parseInt(this.toBinStr(), 2);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (this.readBit) result.append('r'); else result.append('-');
        if (this.writeBit) result.append('w'); else result.append('-');
        if (this.delegateBit) result.append('d'); else result.append('-');

        return result.toString();
    }
}
