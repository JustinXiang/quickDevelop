package com.zhenxinkeji.zhimahuan.utils

import android.text.InputFilter
import android.text.TextUtils
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern


object StringUtils {
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty() || str.trim { it <= ' ' }.equals(
            "null",
            ignoreCase = true
        )
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    fun isSuitablePassword(str: String): Boolean {
        var isDigit = false
        var isLowerCase = false
        var isUpperCase = false
        for (i in str.indices) {
            when {
                Character.isDigit(str[i]) -> {
                    isDigit = true
                }
                Character.isLowerCase(str[i]) -> {
                    isLowerCase = true
                }
                Character.isUpperCase(str[i]) -> {
                    isUpperCase = true
                }
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && (isLowerCase || isUpperCase)
    }

    fun encryptionPhone(str: String): String {
        return if (!TextUtils.isEmpty(str) && str.length > 6) {
            val sb = StringBuilder()
            for (i in str.indices) {
                val c: Char = str[i]
                if (i in 3..6) {
                    sb.append('*')
                } else {
                    sb.append(c)
                }
            }
            sb.toString()
        } else {
            str
        }
    }

    fun isEmail(str: String) : Boolean{
        val regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+\$"
        return str.matches(regex.toRegex())
    }

    @Throws(IllegalAccessException::class)
    fun objectToMap(obj: Any): Map<String, Any>? {
        val map: MutableMap<String, Any> =
            HashMap()
        val clazz: Class<*> = obj.javaClass
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            val fieldName = field.name
            val value: Any = field.get(obj)
            map[fieldName] = value
        }
        return map
    }

    /**
     * 禁止EditText输入空格、表情和换行符以及特殊符号&&
     *
     * @param editText EditText输入框
     */
    fun setEditTextInputSpace(editText: EditText) {
        val emoji: Pattern = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        )
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val emojiMatcher: Matcher = emoji.matcher(source)
            //禁止特殊符号
            var speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
            var compile = Pattern.compile(speChat)
            var matcher = compile.matcher(source.toString())
            //禁止输入空格
            if (source == " " || source.toString().contentEquals("\n")) {
                ""
                //禁止输入表情
            } else if (emojiMatcher.find()) {
                ""
            } else if (matcher.find()){
                ""
            } else {
                null
            }
        }
        editText.filters = arrayOf(filter)
    }

}
