package com.example.samsung.p0621_alertdialogitems;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    final int DIALOG_ITEMS = 1,
              DIALOG_ADAPTER = 2,
              DIALOG_CURSOR = 3;
    int cnt = 0;
    String[] data;
    DB db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        data = getResources().getStringArray(R.array.data);
        db = new DB(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);

    }

    public void onClickButton(View view) {
        chengeCount();
        switch (view.getId()) {
            case R.id.btnItems :
                showDialog(DIALOG_ITEMS);
                break;
            case R.id.btnAdapter :
                showDialog(DIALOG_ADAPTER);
                break;
            case R.id.btnCursor :
                showDialog(DIALOG_CURSOR);
                break;
            default :
                break;
        }
    }

    //Изменение значения счётчика
    private void chengeCount() {
        cnt++;
        //Обновление массива
        data[3] = String.valueOf(cnt);
        //Обновляем БД
        db.chengeRec(4, data[3]);
        cursor.requery();
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            //Диалог из Массива
            case DIALOG_ITEMS :
                adb.setTitle(R.string.items);
                adb.setItems(data, myClicListener);
                break;
            //Диалог из Адаптера
            case DIALOG_ADAPTER :
                adb.setTitle(R.string.adapter);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.select_dialog_item, data
                );
                adb.setAdapter(adapter, myClicListener);
                break;
            //Диалог из Курсора
            case DIALOG_CURSOR :
                adb.setTitle(R.string.cursor);
                adb.setCursor(cursor, myClicListener, DB.COLUMN_TXT);
                break;
        }
        return adb.create();
    }

// For Android below 3.2
    protected void onPrepareDialog(int id, Dialog dialog) {
        //Доступ к адаптеру списка диалога
        AlertDialog aDialog = (AlertDialog) dialog;
        ListAdapter lAdapter = aDialog.getListView().getAdapter();

        switch (id) {
            case DIALOG_ITEMS :
            case DIALOG_ADAPTER :
                //Проверка возможности преобразования
                if (lAdapter instanceof BaseAdapter) {
                    //Преобразование и вызов мета-уведомления о новых данных
                    BaseAdapter bAdapter = (BaseAdapter) lAdapter;
                    bAdapter.notifyDataSetChanged();
                }
                break;
            case DIALOG_CURSOR :
                break;
            default :
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    //Обработчик нажатия на пункт списка в диалоге
    DialogInterface.OnClickListener myClicListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            //Вывод в лог позиции нажатого элемента
            String message = "Rowse number = " + which;
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, message);
        }
    };
}
