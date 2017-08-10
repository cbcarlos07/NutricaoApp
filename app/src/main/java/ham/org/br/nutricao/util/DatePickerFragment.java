package ham.org.br.nutricao.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ham.org.br.nutricao.fragment.PesquisarFragment;

/**
 * Created by carlos.bruno on 08/08/2017.
 */

public class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener{
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
        Intent i = new Intent();
        i.putExtra( "selectedDate",data );

        getTargetFragment().onActivityResult( getTargetRequestCode(), Activity.RESULT_OK, i );

    }



}
