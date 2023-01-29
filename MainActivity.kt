package com.kyle.sample0128

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var myHelper: myDBHelper
    lateinit var edtName: EditText
    lateinit var edtAge: EditText
    lateinit var edtNameResult: EditText
    lateinit var edtAgeResult: EditText
    lateinit var btnInit: Button
    lateinit var btnInsert: Button
    lateinit var btnUpdate: Button
    lateinit var btnDelete:Button
    lateinit var btnSelect: Button
    lateinit var sqlDB: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="친구 목록 DB"

        edtName = findViewById<EditText>(R.id.edtName)
        edtAge = findViewById<EditText>(R.id.edtAge)
        edtNameResult = findViewById<EditText>(R.id.edtNameResult)
        edtAgeResult = findViewById<EditText>(R.id.edtAgeResult)

        btnInit = findViewById<Button>(R.id.btnInit)
        btnInsert = findViewById<Button>(R.id.btnInsert)
        btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnDelete = findViewById<Button>(R.id.btnDelete)
        btnSelect = findViewById<Button>(R.id.btnSelect)

        myHelper = myDBHelper(this)

        // 초기화
        btnInit.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            myHelper.onUpgrade(sqlDB, 1,2)
            sqlDB.close()
            Toast.makeText(applicationContext, "초기화 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 입력
        btnInsert.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            sqlDB.execSQL(" INSERT INTO friendTBL VALUES('"
                    + edtName.text.toString() + "', "
                    + edtAge.text.toString() + ");")
            sqlDB.close()
            Toast.makeText(applicationContext, "입력 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 수정
        btnUpdate.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            if(edtName.text.toString() != ""){
                sqlDB.execSQL("UPDATE friendTBL SET age = "
                + edtAge.text + " WHERE name = '"
                + edtName.text.toString() + "';")
            }
            sqlDB.close()
            Toast.makeText(applicationContext, "수정 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 삭제
        btnDelete.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            if(edtName.text.toString() != ""){
                sqlDB.execSQL(" DELETE FROM friendTBL SET name = '"
                        + edtName.text.toString() + "';")

            }
            sqlDB.close()
            Toast.makeText(applicationContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 조회
        btnSelect.setOnClickListener {
            sqlDB = myHelper.readableDatabase
            var cursor: Cursor
            cursor = sqlDB.rawQuery("SELECT * FROM friendTBL;", null)

            var strName = "친구 이름" + "\r\n" +  "=======" + "\r\n"
            var strAge = "나    이" + "\r\n" +  "=====" + "\r\n"

            while(cursor.moveToNext()) {
                strName += cursor.getString(0) + "\r\n"
                strAge += cursor.getString(1) + "\r\n"
            }

            edtNameResult.setText(strName)
            edtAgeResult.setText(strAge)

            cursor.close()
            sqlDB.close()
        }

    }

    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "infoDB", null, 1){
        override fun onCreate(db: SQLiteDatabase?) { // table을 생성
            db!!.execSQL("CREATE TABLE friendTBL(name char(30), age INTEGER);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS friendTBL") // table 삭제 후 새로 생성하는 방식이다.
            onCreate(db)
        }
    }
}