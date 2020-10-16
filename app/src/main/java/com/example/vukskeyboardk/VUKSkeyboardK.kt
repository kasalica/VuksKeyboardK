@file:Suppress("DEPRECATION")

package com.example.VUKSkeyboardK

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.vukskeyboardk.database.DataBaseHandler


class VUKSkeyboardK : InputMethodService(), OnKeyboardActionListener {
    private var kv: KeyboardView? = null
    private var keyboard_normal: Keyboard? = null
    private var keyboard_symbols : Keyboard? = null
    private var keyboard_symbols2 : Keyboard? = null
    private val caps = false
    private var isCaps = false
    private var keyList: List<Keyboard.Key>? = null

    var pastkey = ""
    var time = 0.toLong()


    fun retrieveKeys() {
        keyList = kv!!.keyboard.keys
    }


    val context = this
    var DB =
        DataBaseHandler(this, "DB", null, 1)
    var DBtemp =
        DataBaseHandler(this, "DBtemp", null, 1)


    @SuppressLint("ClickableViewAccessibility")

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        pastkey = ""
        time = 0.toLong()
    }

    override fun onCreateInputView(): View {
        kv = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView?
        keyboard_normal = Keyboard(this, R.xml.russian)
        keyboard_symbols = Keyboard(this, R.xml.symbols)
        keyboard_symbols2 = Keyboard(this, R.xml.symbols2)

        kv!!.keyboard = keyboard_normal

        kv!!.setOnKeyboardActionListener(this)
        kv!!.isPreviewEnabled = false

        retrieveKeys()

        kv!!.setOnTouchListener { _, event ->
            // For each key in the key list

            for (k in keyList!!) {
                if (event.action == MotionEvent.ACTION_UP) {
                    // If the coordinates from the Motion event are inside of the key
                    if (k.isInside(event.x.toInt(), event.y.toInt())) {
                        // k is the key pressed
                        var centreX: Float
                        var centreY: Float
                        centreX = k.width / 2 + k.x.toFloat()
                        centreY = k.height / 2 + k.y.toFloat()
                        // These values are relative to the Keyboard View

                        DB.insertData1(k.label.toString())

                        if(pastkey!="") {
                            time = System.currentTimeMillis() - time
                            DB.insertData2(pastkey + k.label.toString(), time.toInt())
                        }
                        //we count press coordinates, not release!!!
                        DB.insertData3(k.label.toString(),event.x-centreX, centreY-event.y)

                        pastkey = k.label.toString()
                        time = System.currentTimeMillis()

                    }
                }
            }
            false
        }
        return kv!!
    }

    override fun onPress(i: Int) {}
    override fun onRelease(i: Int) {}

    override fun onKey(i: Int, ints: IntArray) {
        val ic = currentInputConnection
        when (i) {

            KEYCODE_SYMBOLS -> {
                kv!!.keyboard = keyboard_symbols
                retrieveKeys()
                return
            }

            KEYCODE_SYMBOLS2 -> {
                kv!!.keyboard = keyboard_symbols2
                retrieveKeys()
                return
            }

            KEYCODE_BACK -> {
                kv!!.keyboard = keyboard_normal
                retrieveKeys()
                return
            }

            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps
                keyboard_normal!!.isShifted = isCaps
                kv!!.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_ENTER
                )
            )
            else -> {
                var code = i.toChar()
                if (Character.isLetter(code) && isCaps) code =
                    Character.toUpperCase(code)
                ic.commitText(code.toString(), 1)
            }
        }
    }

    override fun onText(charSequence: CharSequence) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}


    companion object {
        var KEYCODE_BACK = -777
        var KEYCODE_SYMBOLS = -997
        var KEYCODE_SYMBOLS2 = -998
    }
}