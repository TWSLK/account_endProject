package com.account.work.helper;

import android.text.TextUtils;
import android.widget.EditText;

import com.account.work.utils.LogUtils;

/**
 * <p>Amount input assistant</p>
 * <p>rule:</p>
 * 1. There is only one decimal point <
 * 2. There are only 1 bits in decimal places 444.4 433.2
 * 3. The maximum amount is 999999999 length: maxLength <
 * 4. At first you can't input 0, and then enter other numbers, such as 020400 <
 * 5. You can't enter decimal points at first...<
 */

public class MoneyEditHelper {

    private EditText editText;
    private String moneyText = "";
    private int maxLength = 9;

    public MoneyEditHelper(EditText editText) {
        this.editText = editText;
    }

    public String onAddNumber(String number) {
        // The maximum length should not be greater than maxLength
        if (moneyText.length() >= maxLength) {
            return moneyText;
        }

        if (number.equals(".")) {
            return isPoint();
        } else {
            return isNumber(number);
        }
    }

    private String isNumber(String number) {
        // Only one input is 0, no more digits are allowed
        if (moneyText.equals("0")) {
            return moneyText;
        }
        // There is only one decimal
        if (moneyText.matches("[0-9]+\\.[0-9]")) {
            System.out.println(moneyText + "  There is already a decimal");
            return moneyText;
        }
        moneyText += number;
        editText.setText(moneyText);
        return moneyText;
    }

    private String isPoint() {
        // Cannot start decimal point at first
        if (TextUtils.isEmpty(moneyText)) {
            return moneyText;
        }
        // You cannot enter multiple decimal points
        if (moneyText.contains(".")) {
            return moneyText;
        }
        moneyText += ".";
        editText.setText(moneyText);
        return moneyText;
    }

    public String onDelete() {
        if (moneyText.length() > 0) {
            moneyText = moneyText.substring(0, moneyText.length() - 1);
        }
        editText.setText(moneyText);
        return moneyText;
    }


    private float getMoney() {
        String moneyString = editText.getText().toString();
        return getFloat(moneyString);
    }

    private float getFloat(String str) {

        float moneyFloat = 0;
        try {
            moneyFloat = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(this, "string to float fail !");
        }
        return moneyFloat;
    }

}
