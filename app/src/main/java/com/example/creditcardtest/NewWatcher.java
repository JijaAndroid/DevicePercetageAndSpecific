package com.example.creditcardtest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import com.example.creditcardtest.StripeEditText.ExpiryDateEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewWatcher extends AppCompatActivity {


    String mLastInput = "";
    ExpiryDateEditText edtExpiry;
    private boolean mValidateCard = true;


    public static String handleExpiration(String month, String year) {

        return handleExpiration(month + year);
    }


    public static String handleExpiration(@NonNull String dateYear) {

        String expiryString = dateYear.replace("/", "");

        String text;
        if (expiryString.length() >= 2) {
            String mm = expiryString.substring(0, 2);
            String yy;
            text = mm;

            try {
                if (Integer.parseInt(mm) > 12) {
                    mm = "12"; // Cannot be more than 12.
                }
            } catch (Exception e) {
                mm = "01";
            }

            if (expiryString.length() >= 4) {
                yy = expiryString.substring(2, 4);

                try {
                    Integer.parseInt(yy);
                } catch (Exception e) {

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    yy = String.valueOf(year).substring(2);
                }

                text = mm + "/" + yy;

            } else if (expiryString.length() > 2) {
                yy = expiryString.substring(2);
                text = mm + "/" + yy;
            }
        } else {
            text = expiryString;
        }

        return text;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_watcher);

        edtExpiry = findViewById(R.id.edtExpiry);


        //edtExpiry.setFilters(new InputFilter[]{new CreditCardExpiryInputFilter()});


        /*cardExpiryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence p0, int start, int removed, int added) {
                if (start == 1 && start+added == 2 && !p0.toString().contains("/")) {
                    cardExpiryView.setText(p0.toString() + "/");
                } else if (start == 3 && start-removed == 2 && p0.toString().contains("/")) {
                    cardExpiryView.setText(p0.toString().replace("/", ""));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        /*cardExpiryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().replace("/", "");

                String month, year = "";
                if (text.length() >= 2) {
                    month = text.substring(0, 2);

                    if (text.length() > 2) {
                        year = text.substring(2);
                    }

                    if (mValidateCard) {
                        int mm = Integer.parseInt(month);

                        if (mm <= 0 || mm >= 13) {
                            //cardExpiryView.setError(getString(R.string.error_invalid_month));
                            return;
                        }

                        if (text.length() >= 4) {

                            int yy = Integer.parseInt(year);

                            final Calendar calendar = Calendar.getInstance();
                            int currentYear = calendar.get(Calendar.YEAR);
                            int currentMonth = calendar.get(Calendar.MONTH) + 1;

                            int millenium = (currentYear / 1000) * 1000;


                            if (yy + millenium < currentYear) {
                                //cardExpiryView.setError(getString(R.string.error_card_expired));
                                return;
                            } else if (yy + millenium == currentYear && mm < currentMonth) {
                                // cardExpiryView.setError(getString(R.string.error_card_expired));
                                return;
                            }
                        }
                    }

                } else {
                    month = text;
                }

                int previousLength = cardExpiryView.getText().length();
                int cursorPosition = cardExpiryView.getSelectionEnd();

                text = handleExpiration(month, year);

                cardExpiryView.removeTextChangedListener(this);
                cardExpiryView.setText(text);
                cardExpiryView.setSelection(text.length());
                cardExpiryView.addTextChangedListener(this);

                int modifiedLength = text.length();

                if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
                    cardExpiryView.setSelection(cursorPosition);
                }

                //onEdit(text);

                if (text.length() == 5) {
                    //onComplete();
                }
            }
        });*/


        /*mExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    formatCardExpiringDate(s);
                } catch (NumberFormatException e) {
                    s.clear();
                    //Toast message here.. Wrong date formate

                }
            }
        });*/

        /*mExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String input = s.toString();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.GERMANY);
                Calendar expiryDateDate = Calendar.getInstance();
                try {
                    expiryDateDate.setTime(formatter.parse(input));
                }  catch (java.text.ParseException e) {
                    if (s.length() == 2 && !mLastInput.endsWith("/")) {
                        int month = Integer.parseInt(input);
                        if (month <= 12) {
                            mExpiryDate.setText(mExpiryDate.getText().toString() + "/");
                            mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                        }
                    } else if (s.length() == 2 && mLastInput.endsWith("/")) {
                        int month = Integer.parseInt(input);
                        if (month <= 12) {
                            mExpiryDate.setText(mExpiryDate.getText().toString().substring(0, 1));
                            mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                        } else {
                            mExpiryDate.setText("");
                            mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                            Toast.makeText(getApplicationContext(), "Enter a valid month", Toast.LENGTH_LONG).show();
                        }
                    } else if (s.length() == 1) {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            mExpiryDate.setText("0" + mExpiryDate.getText().toString() + "/");
                            mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                        }
                    }
                    mLastInput = mExpiryDate.getText().toString();
                    return;
                }
            }

        });*/


    }


    boolean isSlash = false; //class level initialization

    /*private void formatCardExpiringDate(Editable s) {
        String input = s.toString();
        String mLastInput = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        Calendar expiryDateDate = Calendar.getInstance();

        try {
            expiryDateDate.setTime(formatter.parse(input));
        } catch (java.text.ParseException e) {
            if (s.length() == 2 && !mLastInput.endsWith("/") && isSlash) {
                isSlash = false;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    mExpiryDate.setText(mExpiryDate.getText().toString().substring(0, 1));
                    mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                } else {
                    s.clear();
                    mExpiryDate.setText("");
                    mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                    Toast.makeText(getApplicationContext(), "Enter a valid month", Toast.LENGTH_LONG).show();
                }
            } else if (s.length() == 2 && !mLastInput.endsWith("/") && !isSlash) {
                isSlash = true;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    mExpiryDate.setText(mExpiryDate.getText().toString() + "/");
                    mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                } else if (month > 12) {
                    mExpiryDate.setText("");
                    mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                    s.clear();
                    //_toastMessage("invalid month", context);
                }


            } else if (s.length() == 1) {

                int month = Integer.parseInt(input);
                if (month > 1 && month < 12) {
                    isSlash = true;
                    mExpiryDate.setText("0" + mExpiryDate.getText().toString() + "/");
                    mExpiryDate.setSelection(mExpiryDate.getText().toString().length());
                }
            }

            mLastInput = mExpiryDate.getText().toString();
            return;
        }
    }*/

    public class CreditCardExpiryInputFilter implements InputFilter {

        private final String currentYearLastTwoDigits;

        public CreditCardExpiryInputFilter() {
            currentYearLastTwoDigits = new SimpleDateFormat("yy", Locale.US).format(new Date());
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //do not insert if length is already 5
            if (dest != null & dest.toString().length() == 5) return "";
            //do not insert more than 1 character at a time
            if (source.length() > 1) return "";
            //only allow character to be inserted at the end of the current text
            if (dest.length() > 0 && dstart != dest.length()) return "";

            //if backspace, skip
            if (source.length() == 0) {
                return source;
            }

            //At this point, `source` is a single character being inserted at `dstart`.
            //`dstart` is at the end of the current text.

            final char inputChar = source.charAt(0);

            if (dstart == 0) {
                //first month digit
                if (inputChar > '1') return "";
            }
            if (dstart == 1) {
                //second month digit
                final char firstMonthChar = dest.charAt(0);
                if (firstMonthChar == '0' && inputChar == '0') return "";
                if (firstMonthChar == '1' && inputChar > '2') return "";

            }
            if (dstart == 2) {
                final char currYearFirstChar = currentYearLastTwoDigits.charAt(0);
                if (inputChar < currYearFirstChar) return "";
                return "/".concat(source.toString());
            }
            if (dstart == 4){
                final String inputYear = ""+dest.charAt(dest.length()-1)+source.toString();
                if (inputYear.compareTo(currentYearLastTwoDigits) < 0) return "";
            }

            return source;
        }
    }

}
