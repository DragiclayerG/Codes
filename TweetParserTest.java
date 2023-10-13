package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
public class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton(String word) {
        List<String> l = new LinkedList<String>();
        l.add(word);
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<String>();
        for (String s : words) {
            l.add(s);
        }
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    public void removeURLsTest() {
        assertEquals("abc . def.", TweetParser.removeURLs("abc http://www.cis.upenn.edu. def."));
        assertEquals("abc", TweetParser.removeURLs("abc"));
        assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
        assertEquals("abc .", TweetParser.removeURLs("abc http://www.cis.upenn.edu."));
        assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
        assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
        assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
        assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
    }

    @Test
    public void testCleanWord() {
        assertEquals("abc", TweetParser.cleanWord("abc"));
        assertEquals("abc", TweetParser.cleanWord("ABC"));
        assertNull(TweetParser.cleanWord("@abc"));
        assertEquals("ab'c", TweetParser.cleanWord("ab'c"));
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    /* **** ****** ***** **** EXTRACT COLUMN TESTS **** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testExtractColumnGetsCorrectColumn() {
        assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                )
        );
    }

    @Test
    public void testExtractColumnWithNullNegPoints() {
        assertNull(TweetParser.extractColumn(
                        null, -2
                )
        );
    }

    @Test
    public void testExtractColumnWithNullPoints() {
        assertNull(TweetParser.extractColumn(
                        null, 2
                )
        );
    }

    @Test
    public void testExtractColumnWithEmptyString() {
        assertEquals(
                "",
                TweetParser.extractColumn(
                        "", 0
                )
        );
    }

    @Test
    public void testExtractColumnWithSymbols() {
        assertEquals(
                "GoodAfterNoon!.!",
                TweetParser.extractColumn(
                        "GoodAfterNoon!.!", 0
                )
        );
    }

    /* **** ****** ***** ***** CSV DATA TO TWEETS ***** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsWithOneCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        expected.add(" The end should come here.");
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsThatEmpty() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsThatWithoutColumn() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        expected.add(" The end should come here.");
        assertEquals(expected, tweets);
    }

    /* **** ****** ***** ** PARSE AND CLEAN SENTENCE ** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanPerfectWords() {
        List<String> sentence = TweetParser.parseAndCleanSentence("time to sleep");
        List<String> expected = new LinkedList<>();
        expected.add("time");
        expected.add("to");
        expected.add("sleep");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceWithRepeatWords() {
        List<String> sentence = TweetParser.parseAndCleanSentence("one one one sheep testing");
        List<String> expected = new LinkedList<String>();
        expected.add("one");
        expected.add("one");
        expected.add("one");
        expected.add("sheep");
        expected.add("testing");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceWithSpaces() {
        List<String> sentence = TweetParser.parseAndCleanSentence("last  summer  homework");
        List<String> expected = new LinkedList<>();
        expected.add("last");
        expected.add("summer");
        expected.add("homework");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceWithRandomSymbols() {
        List<String> sentence = TweetParser.parseAndCleanSentence("i love $%%^@@$%@% icecream");
        List<String> expected = new LinkedList<>();
        expected.add("i");
        expected.add("love");
        expected.add("icecream");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceEmpty() {
        List<String> sentence = TweetParser.parseAndCleanSentence("");
        List<String> expected = new LinkedList<>();
        assertEquals(expected, sentence);
    }

    /* **** ****** ***** **** PARSE AND CLEAN TWEET *** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanEmptyWriting() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("");
        List<List<String>> expected = new LinkedList<>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetOneSentence() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("one two three.");
        List<List<String>> expected = new LinkedList<>();
        List<String> listTest = new LinkedList<>();
        listTest.add("one");
        listTest.add("two");
        listTest.add("three");
        expected.add(listTest);
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetSameExactSentence() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("i want icecream. i want icecream.");
        List<List<String>> expected = new LinkedList<>();
        List<String> listTest = new LinkedList<>();
        listTest.add("i");
        listTest.add("want");
        listTest.add("icecream");
        expected.add(listTest);
        expected.add(listTest);
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetDifferentPunctuation() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("i love money! lots of money?");
        List<List<String>> expected = new LinkedList<>();
        List<String> listTest = new LinkedList<>();
        listTest.add("i");
        listTest.add("love");
        listTest.add("money");
        expected.add(listTest);
        List<String> listTest2 = new LinkedList<>();
        listTest2.add("lots");
        listTest2.add("of");
        listTest2.add("money");
        expected.add(listTest2);
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetWithRandomSymbols() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("!@#$%^&*&^%#");
        List<List<String>> expected = new LinkedList<>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetLotsOfWords() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("i want to cry. why so much testing.");
        List<List<String>> expected = new LinkedList<>();
        List<String> l1 = new LinkedList<>();
        l1.add("i");
        l1.add("want");
        l1.add("to");
        l1.add("cry");
        expected.add(l1);
        List<String> l2 = new LinkedList<>();
        l2.add("why");
        l2.add("so");
        l2.add("much");
        l2.add("testing");
        expected.add(l2);
        assertEquals(expected, sentences);
    }

    /* **** ****** ***** ** CSV DATA TO TRAINING DATA ** ***** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTrainingDataSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataEmptyCSV() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataSameWordsCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with with with duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with with with duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataWithSymbolsCSV() {
        StringReader sr = new StringReader(
                "0, The end should come &%^$* here.\n" +
                        "1, This comes from data with  $&^$*@with wi^#th duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataExactCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, repeat repeat repeat words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("repeat repeat repeat words".split(" ")));
        assertEquals(expected, tweets);
    }
}