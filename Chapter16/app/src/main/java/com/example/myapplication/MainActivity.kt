package com.example.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var ed_book: EditText
    private lateinit var ed_price: EditText
    private lateinit var btn_query: Button
    private lateinit var btn_insert: Button
    private lateinit var btn_update: Button
    private lateinit var btn_delete: Button

    private lateinit var listView: ListView
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }
    /*

    btn_query   = findViewById(R.id.btn_query)
    btn_insert  = findViewById(R.id.btn_insert)
    btn_update  = findViewById(R.id.btn_update)
    btn_delete  = findViewById(R.id.btn_delete)
    listView    = findViewById(R.id.listView)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter

        dbrw = MyDBHelper(this).writableDatabase

        setListener()
    }
    private fun setListener() {
        val ed_book     = findViewById<EditText>(R.id.ed_book)
        val ed_price    = findViewById<EditText>(R.id.ed_price)

        findViewById<Button>(R.id.btn_insert).setOnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1)
                Toast.makeText(this,"欄位請勿留空",Toast.LENGTH_SHORT).show()
            else
                try {
                    dbrw.execSQL(
                        "INSERT INTO myTable(book, price) VALUES(?,?)",
                        arrayOf(ed_book.text.toString(),
                            ed_price.text.toString())
                    )
                    Toast.makeText(this,"新增:${ed_book.text},價格:${ed_price.text}",Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this,"新增失敗:$e",Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<Button>(R.id.btn_update).setOnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1)
                Toast.makeText(this,"欄位請勿留空",Toast.LENGTH_SHORT).show()
            else
                try {
                    dbrw.execSQL("UPDATE myTable SET price = ${ed_price.text} WHERE book LIKE '${ed_book.text}'")
                    Toast.makeText(this,"更新:${ed_book.text},價格:${ed_price.text}",Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this,"更新失敗:$e",Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            if (ed_book.length() < 1)
                Toast.makeText(this,"書名請勿留空",Toast.LENGTH_SHORT).show()
            else
                try {
                    dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '${ed_book.text}'")
                    Toast.makeText(this,"欄位請勿留空刪除:${ed_book.text}",Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this,"刪除失敗:$e",Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<Button>(R.id.btn_query).setOnClickListener {

            val queryString = if (ed_book.length() < 1)
                "SELECT * FROM myTable"
            else
                "SELECT * FROM myTable WHERE book LIKE '${ed_book.text}'"

            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst()
            items.clear()
            Toast.makeText(this,"共有${c.count}筆資料",Toast.LENGTH_SHORT).show()

            for (i in 0 until c.count) {
                items.add("書名:${c.getString(0)}\t\t\t\t 價格:${c.getInt(1)}")
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }
    }

}