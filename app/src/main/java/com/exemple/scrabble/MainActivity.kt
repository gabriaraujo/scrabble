package com.exemple.scrabble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Title: Scrabble
 *
 * Author: Gabriel Caetano Araújo
 *
 * Brief:
 * The logic of the program is based on going through a tree [Trie] in search
 * of finding all possible words that can be formed with the characters entered.
 * After all the words have been found, the algorithm goes through a refinement
 * phase, in which the selected words are validated and those that do not
 * respect the frequency of characters are discarded. After this refinement,
 * the point count is made, in which each letter that makes up the word is
 * verified to assign the final score and after calculating the value of all
 * words, the word with the highest score is returned.
 *
 * Data Structure Used:
 * A [Trie] tree, or prefix tree, is an ordered tree that can be used to store
 * an array where the keys are usually strings. All descendants of any node have
 * a common prefix with the chain associated with that node, and the root is
 * associated with the empty chain. Normally, values ​​are not associated with
 * all nodes, only with the leaves and some internal nodes that correspond to
 * keys of interest.
 *
 * The choice of this data structure is due to the fact that its search is very
 * fast, since at each level of the tree the combinatorial nature of the problem
 * decreases, since it filters possible combinations of characters according to
 * the available dictionary.
 *
 * Input handling:
 * The processing of data entry is carried out before the search begins. Special
 * characters are removed and saved to be added to unused characters in the
 * future. Initially, the received string is converted to lowercase letters,
 * then it is passed to the filtering process, in which the special characters
 * are removed, and finally it is sent to the search function.
 *
 * Word Filtering:
 * After the search has been carried out, there is a set of strings with all the
 * words found, these words in turn need to be validated, as the search does not
 * consider the frequency of the characters, but their existence. Therefore, for
 * each word found, it is verified that the characters found respect the
 * frequency of the characters received in the input and then the valid strings
 * are returned. Here is an example, if the entry is "alban", it would be
 * possible to form the words "banal", "ban" and "banana", but in the word
 * "banana" some characters are repeated more often than those that were made
 * available, then the word "banana" is discarded.
 *
 * Score Count:
 * After the search and filtering have been performed, you have a set of strings
 * with all valid words. Each word in the set is traversed to assign its score
 * per character after all words have been checked for a set of pairs of
 * (word, points). This set is ordered by the size of the words, from smallest
 * to largest, and then the word with the highest score is applicable, if there
 * is a tie, the first occurrence of that value is returned, that is, the
 * smallest word.
 *
 * Displaying Results:
 * After all the processes have been scheduled, the word with the highest score
 * and all the characters that were not used to compose it are applied on the
 * screen.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializes the tree with the values present in the dictionary
        Trie.insert()
    }
}