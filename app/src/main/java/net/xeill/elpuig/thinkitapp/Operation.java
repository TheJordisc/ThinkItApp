package net.xeill.elpuig.thinkitapp;

/**
 * Created by jsola on 27/12/2017.
 */

public class Operation {
    public enum OpType {
        SUM,SUBST,DIV,MUL
    }

    private int op1;
    private int op2;
    private OpType opType;
    private int res;

    public Operation(int op1, int op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public Operation() {
    }

    public int getOp1() {
        return op1;
    }

    public void setOp1(int op1) {
        this.op1 = op1;
    }

    public int getOp2() {
        return op2;
    }

    public void setOp2(int op2) {
        this.op2 = op2;
    }

    public OpType getOpType() {
        return opType;
    }

    public String getOpTypeStr() {
        switch (opType) {
            case SUM:
                return "+";
            case SUBST:
                return "-";
            case MUL:
                return "x";
            case DIV:
                return "/";
            default:
                return " ";
        }
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int calculate() {
        switch (opType) {
            case SUM:
                this.res=op1+op2;
                break;
            case SUBST:
                this.res=op1-op2;
                break;
            case MUL:
                this.res=op1*op2;
                break;
            case DIV:
                this.res=(int)((float)op1/op2);
                break;
        }
        return this.res;
    }
}
