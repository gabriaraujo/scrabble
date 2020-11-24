package com.exemple.scrabble

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
import java.util.regex.Pattern

/**
 * Title: FirstFragment
 *
 * Author: Gabriel Caetano Araújo
 *
 * Brief:
 * A simple [Fragment] subclass that works as a default destination, responsible
 * for collecting the data entered and displaying the results on the screen.
 *
 * Description:
 * This class is responsible for retrieving the data entered on the screen and
 * passing it to the search functions, properly handled, displaying alerts on
 * screen if there is any inconsistency in the required entry, making the result
 * found available, as well as its score and letters that do not were used.
 */
class FirstFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // performs the main activity of the class if the button was clicked
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            // variable that stores the value entered by the user as input
            var editText: String = view
                    .findViewById<EditText>(R.id.editTextWord).text.toString()

            // performs the input treatment
            val filterChar: Pair<String, CharArray> = normalize(editText)
            editText = filterChar.first

            // generates an alert if the provided value is empty
            if (emptyText(editText)) return@setOnClickListener

            // hides the initial text on the screen
            val textView = view.findViewById<TextView>(R.id.textview_first)
            textView.visibility = View.INVISIBLE

            // performs the word search and saves the results
            val pair: Pair<String, Int>? = Trie.highestScore(editText.toCharArray())
            var rest: String = Trie.getRest(pair!!.first, editText.toCharArray())

            // add unused characters to invalid characters
            rest = rest.plus(String(filterChar.second))

            // displays the result found on the screen
            makeVisible(view, pair, rest)
        }
    }

    /**
     * Brief:
     * Function responsible for displaying the results found on screen.
     *
     * Description:
     * Displays on the screen all elements initially invisible, such as the
     * result fields and their respective labels. Calls the functions that
     * format the strings containing the results.
     *
     * Params:
     *
     *      view (View): View on which the elements then contained.
     *
     *      pair (Pair<String, Int>?): Pair composed of the word with the
     *          highest score and its value.
     *
     *      rest (String): Set with all unused characters.
     */
    private fun makeVisible(view: View, pair: Pair<String, Int>?, rest: String) {
        // makes the label of unused characters visible
        val restLabel = view.findViewById<TextView>(R.id.textview_labelrest)
        restLabel.visibility = View.VISIBLE

        // makes the dash that divides the screen visible
        val divView = view.findViewById<View>(R.id.divider)
        divView.visibility = View.VISIBLE

        // makes the answer label visible and formats its value with the score
        val pointView = view.findViewById<TextView>(R.id.textview_points)
        pointView.visibility = View.VISIBLE
        pointView.text = String.format(getString(R.string.points), pair!!.second)

        // makes the obtained response visible and formats it
        val ansLayout = view.findViewById<LinearLayout>(R.id.layout_ans)
        ansLayout.visibility = View.VISIBLE
        showAnswer(ansLayout, pair.first)

        // makes unused characters visible and formats them
        val restLayout = view.findViewById<LinearLayout>(R.id.layout_rest)
        restLayout.visibility = View.VISIBLE
        showRest(restLayout, rest)
    }

    /**
     * Brief:
     * Function responsible for formatting the response.
     *
     * Description:
     * Creates a non-clickable button for each letter contained in the response,
     * each button stores a letter and are displayed on the screen forming the
     * word.
     *
     * Params:
     *
     *      ansLayout (LinearLayout): Element in which the buttons created must
     *          be contained.
     *
     *      word (String): Word that must be formatted before being displayed
     *          on the screen.
     */
    private fun showAnswer(ansLayout: LinearLayout, word: String) {
        // clears the screen if any element is already displayed
        ansLayout.removeAllViews()

        // creates a non-clickable button for each letter and displays them on screen
        for (letter in word) {
            val btnPoints = Button(this.context)
            btnPoints.isClickable = false
            btnPoints.text = letter.toString()
            btnPoints.layoutParams = LinearLayout.LayoutParams(
                    120,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )

            ansLayout.addView(btnPoints)
        }
    }

    /**
     * Brief:
     * Function responsible for formatting unused characters
     *
     * Description:
     * Create a non-clickable button for each letter contained in the unused
     * character set, each button stores a letter and are displayed on the
     * screen.
     *
     * Params:
     *
     *      ansLayout (LinearLayout): Element in which the buttons created must
     *          be contained.
     *
     *      rest (String): Set of characters that must be formatted before being
     *          displayed on the screen.
     */
    private fun showRest(restLayout: LinearLayout, rest: String) {
        // clears the screen if any element is already displayed
        restLayout.removeAllViews()

        // creates a non-clickable button for each character and displays them on screen
        for (letter in rest) {
            val btnRest = Button(this.context)
            btnRest.isClickable = false
            btnRest.text = letter.toString()
            btnRest.layoutParams = LinearLayout.LayoutParams(
                    120,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )

            restLayout.addView(btnRest)
        }
    }

    /**
     * Brief:
     * Issues an alert if no valid characters have been entered.
     *
     * Description:
     * After removing the special characters from the entry, this function
     * checks if there are any characters left for the search to be carried out.
     * If there are no characters, an alert is issued warning that there are no
     * valid characters.
     *
     * Params:
     *
     *      str (String): Preprocessed value that will be analyzed.

     *
     * Returns:
     *
     *      (Boolean): Value indicating whether or not there is a valid entry
     *          for the search. It has a value of 'true', if the entry is
     *          invalid, 'false' otherwise.
     */
    private fun emptyText(str: String): Boolean {
        // checks if the string passed by parameter is empty
        if (str.isEmpty()) {
            val dialog = AlertDialog.Builder(this.context)
            dialog.setTitle("Alerta!")
            dialog.setMessage("Nenhum caracter válido inserido!")
            dialog.setPositiveButton("OK", null)
            dialog.show()

            // returns true if the entry is invalid
            return true
        }

        // returns false if the entry is valid
        return false
    }

    /**
     * Resumo:
     * Function responsible for normalizing the input data.
     *
     * Descrição:
     * Performs the treatment of the received characters as input data.
     * Initially it turns all letters to lowercase and saves possible special
     * characters in an auxiliary structure. Then, if there is any special
     * character, an alert is issued that the special characters have been
     * disregarded. Then, special characters are removed, including numbers
     * and blanks.
     *
     * Params:
     *
     *      str (String): String of characters received as input.
     *
     * Returns:
     *
     *      (Pair<String, CharArray>): Pair composed of the duly standardized
     *          entry and the characters that were disregarded.
     */
    private fun normalize(str: String) : Pair<String, CharArray> {
        // turns characters to lowercase
        var text = str.toLowerCase(Locale.getDefault())

        // variable to store the discarded characters
        val rest = arrayListOf<Char>()

        // creates a pattern for special and numeric characters
        val pattern = Pattern.compile("[^A-Za-z0-9 ]")
        val numPattern = Pattern.compile("\\d")

        // saves all characters that fit the pattern
        for (letter in text) if (
                pattern.matcher(letter.toString()).find()
                || numPattern.matcher(letter.toString()).find()
        ) rest.add(letter)

        // issues an alert that some characters will be disregarded
        if (pattern.matcher(text).find() || numPattern.matcher(text).find()) {
            val dialog = AlertDialog.Builder(this.context)
            dialog.setTitle("Alerta!")
            dialog.setMessage("Caracteres inválidos foram desconsiderados!")
            dialog.setPositiveButton("OK", null)
            dialog.show()
        }

        // removes all special characters from the provided entry
        text = Regex("[^A-Za-z0-9 ]").replace(text, "")
        text = text.replace("\\s".toRegex(), "")
        text = text.replace("\\d".toRegex(), "")

        // returns a pair with the formatted entry and the characters removed
        return Pair(text, rest.toCharArray())
    }
}