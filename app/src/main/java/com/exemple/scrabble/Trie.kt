package com.exemple.scrabble

import java.lang.StringBuilder
import java.util.ArrayList

/**
 * Title: Trie
 *
 * Author: Gabriel Caetano Araújo
 *
 * Brief:
 * This object represents the implementation of a Trie tree for a 26 letter
 * alphabet.
 *
 * Descrição:
 * A Trie tree, or prefix tree, is an ordered tree that can be used to store an
 * array where the keys are usually strings. All descendants of any node have a
 * common prefix with the chain associated with that node, and the root is
 * associated with the empty chain. Normally, values ​​are not associated with
 * all nodes, only with the leaves and some internal nodes that correspond to
 * keys of interest.
 */
object Trie {
    const val alphabet = 26
    private var root: TrieNode = TrieNode()

    class TrieNode {
        var child = arrayOfNulls<TrieNode>(alphabet)
        var leaf = false
    }

    /**
     * Brief:
     * Function responsible for inserting the dictionary words in the [Trie]
     * tree.
     *
     * Description:
     * This function inserts all the words in a dictionary into a tree [Trie].
     * For the sake of simplicity, the dictionary is created in a 'hardcoded'
     * way, that is, its data is directly in the code. Each word in this
     * dictionary is added individually to the tree, the length of the string
     * is measured to find the index of each of its letters. The insertion
     * begins at the root of the tree, checking its descendants, if there is
     * already a node for that letter, then the root becomes that node,
     * otherwise a new node is created and defined as the root. Thus, with each
     * letter entered, a level is lowered in the tree. After the word has been
     * completely inserted, the last node is marked as a leaf node, indicating
     * the end of the word. This process is repeated for all words in the
     * dictionary.
     */
    fun insert() {
        // dictionary containing all available words
        val dict = arrayOf(
            "pineapple", "herd", "order", "door", "table", "die", "mangoes",
            "go", "things", "radiography", "math", "drugs", "buildings",
            "implementation", "computer", "balloon", "cup", "boredom", "banner",
            "book", "leave", "superior", "profession", "meeting", "buildings",
            "mountain", "botany", "bathroom", "boxes", "cursing", "infestation",
            "termite", "winning", "breaded", "rats", "noise", "antecedent",
            "company", "emissary", "slack", "break", "guava", "free",
            "hydraulic", "man", "dinner", "games", "assembly", "manual",
            "cloud", "snow", "operation", "yesterday", "duck", "foot", "trip",
            "cheese", "room", "backyard", "loose", "route", "jungle", "tattoo",
            "tiger", "grape", "last", "reproach", "voltage", "angry", "mockery",
            "pain"
        )

        // performs the individual insertion of each word in the tree
        for (key in dict) {
            // saves the word size and the root node
            val length = key.length
            var crawl = root

            // inserts each letter of the word in the tree
            for (i in 0 until length) {
                val index = key[i] - 'a'
                if (crawl.child[index] == null) crawl.child[index] = TrieNode()
                crawl = crawl.child[index]!!
            }

            // marks the current node as a leaf node
            crawl.leaf = true
        }
    }

    /**
     * Brief:
     * Function responsible for calculating the word score.
     *
     * Description:
     * Calculates the value of each valid word obtained from the input
     * characters. For each string, its characters are traversed and the value
     * assigned to them is added to an array of points, initialized with zeros,
     * in the index corresponding to the word in question. The set of words and
     * points are combined to form a set of pairs of (word, points). This set
     * is ordered by the size of the words, from smallest to largest, and then
     * the word with the highest score is selected; if there is a tie, the first
     * occurrence of that value is returned, that is, the smallest word.
     *
     * Params:
     *
     *      arr (CharArray): Array of characters received as input for word
     *          formation.
     *
     * Returns:
     *
     *      (Pair<String, Int>?): Pair composed of the word with the highest
     *          score and its value.
     */
    fun highestScore(arr: CharArray): Pair<String, Int>? {
        // auxiliary variables for calculating the value of each letter
        val one = arrayOf('e', 'a', 'i', 'o', 'n', 'r', 't', 'l', 's', 'u')
        val two = arrayOf('w', 'd', 'g')
        val tree = arrayOf('b', 'c', 'm', 'p')
        val four = arrayOf('f', 'h', 'v')
        val eight = arrayOf('j', 'x')
        val ten = arrayOf('q', 'z')

        // searches for all words that can be formed from the entry
        val words = searchAllWords(arr)

        // filters the words to respect the frequency of characters
        wordFilter(arr, words)

        // calculates the value of each word and saves it in an array of points
        val points = IntArray(words.size)
        for (i in words.indices) for (letter in words[i])
            when (letter) {
                in one -> points[i] += 1
                in two -> points[i] += 2
                in tree -> points[i] += 3
                in four -> points[i] += 4
                in eight -> points[i] += 8
                in ten -> points[i] += 10
            }

        // associates each word with its respective score
        var pair = words zip points.toTypedArray()

        // sorts pairs of (word, dots) by the length of each word
        pair = pair.sortedBy { it.first.length }

        // returns the first occurrence of the pair with the highest score
        return pair.maxBy { it.second }
    }

    /**
     * Brief:
     * Function responsible for finding the characters that were not used for
     * the composition of the final word.
     *
     * Description:
     * This function finds and saves all the characters that were not used for
     * the formation of the word with the highest score. From the input
     * characters and the chosen word, each letter of the word is traversed and
     * removes its first occurrence from the input set, so at the end of this
     * execution, there is a set with only the letters that were not used.
     *
     * Params:
     *
     *      word (String): Highest scored word.
     *
     *      arr (CharArray): Array of characters received as input for word
     *          formation.
     *
     * Returns:
     *
     *      (String): Set with all unused characters.
     */
    fun getRest(word: String, arr: CharArray) : String {
        // auxiliary variable to store the input characters
        val rest = arr.toMutableList()

        // removes each character in the word from the input characters
        for (letter in word) rest.remove(letter)

        // returns all unused characters
        return String(rest.toCharArray())
    }

    /**
     * Brief:
     * Function responsible for searching all words in the dictionary that can
     * be formed with the input characters.
     *
     * Description:
     * Searches for all words contained in the trees that can be formed with
     * the characters provided. Initially an array is created where the words
     * found will be saved and also an array of Booleans for each letter of the
     * alphabet. For each character received in the entry, it is marked as
     * 'true' in the array of Booleans in the index corresponding to that letter
     * in the alphabet. In sequence, this array is traversed and if any position
     * is marked as 'true' and there is a descendant in the tree for that
     * letter, then that letter is added to a string, which is initially empty,
     * so that a deep search can be performed for the descendants of that
     * letter. After all letters that have been received have been searched, the
     * words that were found are returned.
     *
     * Params:
     *
     *      arr (CharArray): Array of characters received as input for word
     *          formation.
     *
     * Returns:
     *
     *      (ArrayList<String>): Set with all the words found by the search.
     */
    private fun searchAllWords(arr: CharArray): ArrayList<String> {
        // initially empty word set and boolean array
        val words = arrayListOf("")
        val hash = BooleanArray(alphabet)

        // marks each letter of the entry as 'true' in the Boolean array
        for (element in arr) hash[element - 'a'] = true
        var str = StringBuilder()

        // scrolls through the alphabet to search each letter in the entry
        for (i in 0 until alphabet) if (hash[i] && root.child[i] != null) {
            // saves the letter that has descendants and is in the entry
            str.append((i + 'a'.toInt()).toChar())

            // performs in-depth search for each letter
            searchWord(root.child[i], words, hash, str.toString())
            str = StringBuilder()
        }

        // returns all words found in the tree
        return words
    }

    /**
     * Brief:
     * Recursive function responsible for searching in depth for each letter of
     * the entry.
     *
     * Description:
     * Searches each node for the existence of descendants and adds the
     * characters found to the string passed by parameter, which initially has
     * only the first letter of the word. If the node in question is a leaf
     * node, it means that a complete word has been found, then that word is
     * added to the set. The search continues until you scroll through all the
     * letters available at the entrance.
     *
     * Params:
     *
     *      root (TrieNode?): Node through which the search will start.
     *
     *      words (ArrayList<String>): Set with all the words found by the
     *          search.
     *
     *      hash (BooleanArray): Set of Booleans that indicate which letters of
     *          the alphabets are present at the entrance.
     *
     *      str (String): Word that will be constructed during the execution of
     *          the search.
     */
    private fun searchWord(
            root: TrieNode?,
            words: ArrayList<String>,
            hash: BooleanArray,
            str: String
    ) {
        // if the node in question is a leaf the word is saved in the set
        if (root!!.leaf) words.add(str)

        // performs the in-depth search for each valid descendant
        for (i in 0 until alphabet) if (hash[i] && root.child[i] != null) {
            val c = (i + 'a'.toInt()).toChar()
            searchWord(root.child[i], words, hash, str + c)
        }
    }

    /**
     * Brief:
     * Function responsible for filtering words that disregard the character
     * frequency of the entry.
     *
     * Description:
     * This function removes from the set of words those that violate the
     * acceptance criteria, in this case the frequency of each character.
     * Initially an array of integers saves how many occurrences there were of
     * each character in the entry, where the index corresponds to its position
     * in the alphabet. This array is then traversed and for each letter whose
     * frequency is not zero, it is verified how many occurrences of it exist
     * in each of the words in the set. If any word has more characters of that
     * type than are available, that word is removed from the set.
     *
     * Params:
     *
     *      arr (CharArray): Array of characters received as input for word
     *          formation.
     *
     *      words (ArrayList<String>): Set with all the words found by the
     *          search.
     */
    private fun wordFilter(arr: CharArray, words: ArrayList<String>) {
        // array that stores the frequency of each letter in the alphabet
        val frequency = IntArray(alphabet)

        // auxiliary variable to store the words that must be removed
        val aux = arrayListOf<String>()

        // the frequency of each letter is calculated and the search begins
        for (element in arr) frequency[element - 'a']++
        for (i in 0 until alphabet) if (frequency[i] > 0)
            for (word in words) {
                // the occurrences of the letter in question are counted in the word
                val n = word.count { c -> c == (i + 'a'.toInt()).toChar() }

                // save the word that must be removed from the set
                if (n > frequency[i]) aux.add(word)
            }

        // remove the invalid words from the solution set
        for (element in aux) words.remove(element)
    }
}