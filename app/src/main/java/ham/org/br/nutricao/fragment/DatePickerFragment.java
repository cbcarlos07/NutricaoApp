package ham.org.br.nutricao.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by carlos.bruno on 15/08/2017.
 */

public class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener{
    private TextView textView;
    public DatePickerFragment( TextView tV ){
        this.textView = tV;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it

        return new DatePickerDialog(  getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // create date object using date set by user
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        /*if (getTargetFragment() == null)
            return;*/

        SimpleDateFormat data_br = new SimpleDateFormat( "dd/MM/yyyy" );
        String data = data_br.format( date );

       // strData = data;
        textView.setText( data );
//            dataAdapter.notifyDataSetChanged();

    }



}