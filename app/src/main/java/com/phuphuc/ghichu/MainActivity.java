package com.phuphuc.ghichu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Database database;

    ListView lvCongViec;
    List<CongViec> congViecList;
    CongViecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listviewCongViec);
        congViecList = new ArrayList<>();
        adapter = new CongViecAdapter(this, R.layout.dong_cong_viec, congViecList);
        lvCongViec.setAdapter(adapter);

        database = new Database(this, "ghichu.sqlite", null, 1);
        database.queryData("CREATE TABLE IF NOT EXISTS CongViec (Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

//        database.queryData("INSERT INTO CongViec VALUES(null, 'Làm bài tập Android')");
//        database.queryData("INSERT INTO CongViec VALUES(null, 'Viết ứng dụng ghi chú')");

        GetDataCongViec();
    }

    private void GetDataCongViec() {
        Cursor dataCongViec = database.getData("SELECT * FROM CongViec");
        congViecList.clear();
        while (dataCongViec.moveToNext()) {
            int id = dataCongViec.getInt(0);
            String ten = dataCongViec.getString(1);
            congViecList.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_cong_viec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {
            showDialogThem();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtTen = dialog.findViewById(R.id.edittextTenCV);
        Button btnThem = dialog.findViewById(R.id.buttonThem);
        Button btnHuy = dialog.findViewById(R.id.buttonHuy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString();
                if (ten.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập công việc của bạn!", Toast.LENGTH_SHORT).show();
                } else {
                    database.queryData("INSERT INTO CongViec VALUES(null, '" + ten + "')");
                    Toast.makeText(MainActivity.this, "Đã thêm công việc của bạn!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showDialogSua(int id, String ten) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtTenCV = dialog.findViewById(R.id.edittextEditTenCV);
        Button btnXacNhan = dialog.findViewById(R.id.buttonXacNhanEdit);
        Button btnHuy = dialog.findViewById(R.id.buttonHuyEdit);

        edtTenCV.setText(ten);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi = edtTenCV.getText().toString().trim();
                if (tenMoi.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Hãy nhập tên công việc của bạn!", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.queryData("UPDATE CongViec SET TenCV = '"+ tenMoi +"' WHERE Id = '"+ id +"'");
                    Toast.makeText(MainActivity.this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showDialogXoa(int id, String ten) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Bạn có chắc muốn xóa công việc \"" + ten + "\" không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.queryData("DELETE FROM CongViec WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "Đã xóa công việc \"" + ten + "\"!", Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}