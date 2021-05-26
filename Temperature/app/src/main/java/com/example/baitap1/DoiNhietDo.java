package com.example.baitap1;

public class DoiNhietDo {
    private int C,F;
    public DoiNhietDo(int c, int f)
    {
        C=c;
        F=f;
    }
    public int getF() { return F;}

    public int getC() {return C;}

    public void setF(int f) {F = f;}

    public void setC(int c) {C = c;}
    public int changeCtoF()
    {
        F = (int)Math.round(1.0 * C * 9 / 5) + 32;
        return F;
    }
    public int changeFtoC()
    {
        C = (int)Math.round(1.0 * ( F - 32 ) * 5 / 9);
        return C;
    }
}
