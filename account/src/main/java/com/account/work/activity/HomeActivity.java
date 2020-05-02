package com.account.work.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.account.work.R;
import com.account.work.activity.base.BaseActivity;
import com.account.work.db.DbHelper;
import com.account.work.helper.EditDialogHelper;
import com.account.work.model.Budget;
import com.account.work.utils.SPUtils;
import com.account.work.utils.building.BindLayout;
import com.account.work.utils.building.BindView;
import com.account.work.widght.PreferenceSwitchView;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Personal homepage
 */

@BindLayout(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_budget)
    LinearLayout llBudget;
    @BindView(R.id.tv_budget)
    TextView tvBudget;

    @BindView(R.id.ll_set_time_line)
    LinearLayout llTimeLine;
    @BindView(R.id.psv_time_line)
    PreferenceSwitchView psvTimeLine;
    @BindView(R.id.chang_pass)
    TextView changePass;
    @BindView(R.id.login_info)
    TextView loginInfo;
    private int yearMonth;


    private Handler handler = new Handler();
    private float budgetValue;

    @Override
    protected void initView() {
        super.initView();
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void initListener() {
        changePass.setOnClickListener(this);
        loginInfo.setOnClickListener(this);
        llBudget.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initYearMonth();
        initBudget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SPUtils.init(this);
        if(!TextUtils.isEmpty(SPUtils.getString("user",""))){
            loginInfo.setText("当前用户："+SPUtils.getString("user",""));
            changePass.setVisibility(View.VISIBLE);
        }else{
            loginInfo.setText("点击登陆");
            changePass.setVisibility(View.GONE);
        }
    }

    private void initYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        yearMonth = year * 100 + month;
    }

    private void initBudget() {
        Budget budget = new DbHelper().queryBudget(yearMonth);
        if (budget != null) {
            budgetValue = budget.getValue();
            tvBudget.setText(getString(R.string.set_budget_show, budgetValue + ""));
        } else {
            tvBudget.setText(getString(R.string.set_budget_show, "还没有设置预算金额"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_budget:
                budgetSetup();
                break;
            case R.id.chang_pass:
                final EditText et1 = new EditText(this);
                final EditText et2 = new EditText(this);
                et1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et1.setHint("原密码");
                et2.setHint("新密码");
                LinearLayout ll = new LinearLayout(this);
                ll.setPadding(65,45,65,45);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(et1);
                ll.addView(et2);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) et1.getLayoutParams();
                p.setMargins(0,30,0,0);
                new AlertDialog.Builder(this).setTitle("修改密码")
                        .setView(ll)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input1 = et1.getText().toString();
                                String input2 = et2.getText().toString();
                                String old = SPUtils.getString(SPUtils.getString("user",""));
                                if(!input1.equalsIgnoreCase(old)){
                                    Toast.makeText(HomeActivity.this, "原密码输入错误", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(HomeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                SPUtils.putString(SPUtils.getString("user",""),input2);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.login_info:
                startActivity(new Intent(this, LoginActivity.class)
                    .putExtra("nojump",true));
                break;
        }
    }

    /**
     * Budget setting
     */
    private void budgetSetup() {
        EditDialogHelper editDialogHelper = new EditDialogHelper(this);
        editDialogHelper.editText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        editDialogHelper.editText.setHint(budgetValue + "");

        editDialogHelper.show("输入预算", new EditDialogHelper.ButtonListener() {
            @Override
            public void onPositive(View editViewRoot, String content) {
                try {
                    float budget = Float.parseFloat(content);
                    if (budget <= 0) {
                        Toasty.error(HomeActivity.this, "输入错误").show();
                        return;
                    }
                    updateBudget(budget);
                    Toasty.success(HomeActivity.this, "修改成功").show();
                    tvBudget.setText(getString(R.string.set_budget_show, budget + ""));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(HomeActivity.this, "输入错误").show();
                    return;
                }
            }
        });

    }

    /**
     * Update budget
     */
    private void updateBudget(float budgetValue) {
        Budget budget = new Budget();
        budget.setValue(budgetValue)
                .setYearMonth(yearMonth);
        new DbHelper().updateBudget(budget);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
