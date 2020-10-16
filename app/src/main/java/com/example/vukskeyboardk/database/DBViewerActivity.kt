package com.example.vukskeyboardk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.VUKSkeyboardK.*

import com.example.vukskeyboardk.database.DataBaseHandler
import com.example.vukskeyboardk.database.*
import kotlinx.android.synthetic.main.db_viewer_activity.*

class DBViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_viewer_activity)

        val context = this
        var db = DataBaseHandler(
            context,
            "DB",
            null,
            1
        )

        btnRead1.setOnClickListener {
            var data = db.readData1()
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).character + "  |  count: " + data.get(i).count + "\n")
            }
        }
        btnRead2.setOnClickListener {
            var data = db.readData2()
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).characters + "  |  time: " + data.get(i).time + "ms  |  count: " + data.get(i).count + "\n")
            }
        }
        btnRead3.setOnClickListener {
            var data = db.readData3()
            listResult.text = ""
            for (i in 0..(data.size - 1)) {
                listResult.append(data.get(i).character + "  |  x = " + data.get(i).x + ", y =  " + data.get(i).y + "  |  count: " + data.get(i).count + "\n")
            }
        }

        btnDeleteDB.setOnClickListener{
            db.deleteData()
            btnRead1.performClick()
        }

        btnUploadDB.setOnClickListener {
        }

        btnTempDB.setOnClickListener {
            val message: String = etUserMessage.text.toString()
            val intent = Intent(this, TempDBViewerActivity::class.java)

            intent.putExtra("user_message", message)
            startActivity(intent)

        }


    }
}
