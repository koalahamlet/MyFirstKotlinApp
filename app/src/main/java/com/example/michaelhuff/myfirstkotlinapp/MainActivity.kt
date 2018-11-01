package com.example.michaelhuff.myfirstkotlinapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

import java.util.*

class MainActivity : AppCompatActivity() {

    private val validExpressions = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parens_et = findViewById<EditText>(R.id.editText)
        val balance_bt = findViewById<Button>(R.id.balance_button)

        balance_bt.setOnClickListener {
            val s = parens_et.text.toString()

            val t = removeInvalidParentheses(s)[0]

            parens_et.setText(t)
        }
    }

    fun removeInvalidParentheses(s: String): List<String> {

        var left = 0
        var right = 0

        // First, we find out the number of misplaced left and right parentheses.
        for (i in 0 until s.length) {

            // Simply record the left one.
            if (s[i] == '(') {
                left++
            } else if (s[i] == ')') {
                // If we don't have a matching left, then this is a misplaced right, record it.
                right = if (left == 0) right + 1 else right

                // Decrement count of left parentheses because we have found a right
                // which CAN be a matching one for a left.
                left = if (left > 0) left - 1 else left
            }
        }

        this.recurse(s, 0, 0, 0, left, right, StringBuilder())
        return ArrayList(this.validExpressions)
    }

    private fun recurse(
        s: String,
        index: Int,
        leftCount: Int,
        rightCount: Int,
        leftRem: Int,
        rightRem: Int,
        expression: StringBuilder) {

        // If we reached the end of the string, just check if the resulting expression is
        // valid or not and also if we have removed the total number of left and right
        // parentheses that we should have removed.
        if (index == s.length) {
            if (leftRem == 0 && rightRem == 0) {
                this.validExpressions.add(expression.toString())
            }

        } else {
            val character = s[index]
            val length = expression.length

            // The discard case.
            // We don't recurse if the remaining count for that parenthesis is == 0.
            if (character == '(' && leftRem > 0 || character == ')' && rightRem > 0) {
                recurse(
                    s,
                    index + 1,
                    leftCount,
                    rightCount,
                    leftRem - if (character == '(') 1 else 0,
                    rightRem - if (character == ')') 1 else 0,
                    expression
                )
            }

            expression.append(character)

            // Simply recurse one step further if the current character is not a parenthesis.
            if (character != '(' && character != ')') {

                recurse(s, index + 1, leftCount, rightCount, leftRem, rightRem, expression)

            } else if (character == '(') {

                // Consider an opening bracket.
                recurse(s, index + 1, leftCount + 1, rightCount, leftRem, rightRem, expression)

            } else if (rightCount < leftCount) {

                // Consider a closing bracket.
                recurse(s, index + 1, leftCount, rightCount + 1, leftRem, rightRem, expression)
            }

            // Finally, delete the char.
            expression.deleteCharAt(length)
        }
    }

}
