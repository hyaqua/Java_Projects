package data;

import javafx.beans.property.SimpleDoubleProperty;

public class Tuple {
    SimpleDoubleProperty total;
    SimpleDoubleProperty interest;
    SimpleDoubleProperty repayment;
    SimpleDoubleProperty remainder;
    public Tuple(double t1, double t2, double t3, double t4) {
        total = new SimpleDoubleProperty(Math.round(t1*100)/100.0);
        interest = new SimpleDoubleProperty(Math.round(t2*100)/100.0);
        repayment = new SimpleDoubleProperty(Math.round(t3*100)/100.0);
        remainder = new SimpleDoubleProperty(Math.round(t4*100)/100.0);
    }
    public Tuple() {
    }
    public double get(int idx) {
        return switch (idx) {
            case 0 -> total.get();
            case 1 -> interest.get();
            case 2 -> repayment.get();
            case 3 -> remainder.get();
            default -> 0.0;
        };
    }

    public void set(int idx, double t) {
        switch (idx) {
            case 0 -> total = new SimpleDoubleProperty(Math.round(t * 100) / 100.0);
            case 1 -> interest = new SimpleDoubleProperty(Math.round(t * 100) / 100.0);
            case 2 -> repayment = new SimpleDoubleProperty(Math.round(t * 100) / 100.0);
            case 3 -> remainder = new SimpleDoubleProperty(Math.round(t * 100) / 100.0);
        }
    }

    public double getTotal() {
        return total.get();
    }

    public SimpleDoubleProperty totalProperty() {
        return total;
    }

    public double getInterest() {
        return interest.get();
    }

    public SimpleDoubleProperty interestProperty() {
        return interest;
    }

    public double getRepayment() {
        return repayment.get();
    }

    public SimpleDoubleProperty repaymentProperty() {
        return repayment;
    }

    public double getRemainder() {
        return remainder.get();
    }

    public SimpleDoubleProperty remainderProperty() {
        return remainder;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public void setInterest(double interest) {
        this.interest.set(interest);
    }

    public void setRepayment(double repayment) {
        this.repayment.set(repayment);
    }

    public void setRemainder(double remainder) {
        this.remainder.set(remainder);
    }
}
