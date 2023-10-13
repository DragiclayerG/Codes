package org.cis1200;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for MarkovChain */
public class MarkovChainTest {

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    @Test
    public void testAddBigramNull() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.addBigram(null, "10"));
    }

    @Test
    public void testAddBigramNumbersAndWordsMultipleAdds() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "hello");
        mc.addBigram("2", "world");
        mc.addBigram("3", "over");
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(mc.chain.containsKey("1"));
        assertTrue(mc.chain.containsKey("2"));
        assertTrue(mc.chain.containsKey("3"));
        assertTrue(pd.getRecords().containsKey("hello"));
        assertTrue(pd2.getRecords().containsKey("world"));
        assertTrue(pd3.getRecords().containsKey("over"));
        assertEquals(1, pd.count("hello"));
        assertEquals(1, pd2.count("world"));
        assertEquals(1, pd3.count("over"));
    }

    @Test
    public void testAddBigramNullAgain() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.addBigram("0", null));
    }

    @Test
    public void testAddBigramMultipleRepeatAdds() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        mc.addBigram("1", "1");
        mc.addBigram("1", "1");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
        assertTrue(pd.getRecords().containsKey("1"));
        assertEquals(2, pd.count("1"));
    }

    /* ***** ****** ***** ***** TRAIN TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testTrain() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainOfDuplicatesWords() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(2, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(2, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(2, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainRepeatMultipleSentence() {
        MarkovChain mc = new MarkovChain();
        String sentence = "i want sleep";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        ProbabilityDistribution<String> pd1 = mc.chain.get("i");
        ProbabilityDistribution<String> pd2 = mc.chain.get("want");
        ProbabilityDistribution<String> pd3 = mc.chain.get("sleep");
        assertEquals(3, mc.chain.size());
        assertTrue(pd1.getRecords().containsKey("want"));
        assertEquals(2, pd1.count("want"));
        assertTrue(pd2.getRecords().containsKey("sleep"));
        assertEquals(2, pd2.count("sleep"));
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(2, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainNull() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.train(null));
    }

    @Test
    public void testTrainEmpty() {
        MarkovChain mc = new MarkovChain();
        String sentence = "";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(0, mc.chain.size());
    }

    @Test
    public void testTrainOneWord() {
        MarkovChain mc = new MarkovChain();
        String sentence = "goodbye";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        ProbabilityDistribution<String> pd = mc.chain.get("goodbye");
        assertEquals(1, mc.chain.size());
        assertTrue(pd.getRecords().containsKey(MarkovChain.END_TOKEN));
    }

    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testWalk() {
        /*
         * Using the sentences "CIS 1200 rocks" and "CIS 1200 beats CIS 1600",
         * we're going to put some bigrams into the Markov Chain.
         *
         * While in the real world, we want the sentence we output to be random,
         * we don't want this in testing. For testing, we want to modify our
         * ProbabilityDistribution such that it will output a predictable chain
         * of words.
         *
         * Luckily, we've provided a `fixDistribution` method that will do this
         * for you! By calling `fixDistribution` with a list of words that you
         * expect to be output, the ProbabilityDistributions will be modified to
         * output your words in that order.
         *
         * See our below test for an example of how to use this.
         */

        String[] expectedWords = { "CIS", "1200", "beats", "CIS", "1200", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        mc.reset("CIS"); // we start with "CIS" since that's the word our desired walk starts with
        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));

        for (int i = 0; i < expectedWords.length; i++) {
            assertTrue(mc.hasNext());
            assertEquals(expectedWords[i], mc.next());
        }

    }

    @Test
    public void testWalkStartLastSentence() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "i want sleep";
        String sentence2 = "finish summer homework";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        mc.reset(MarkovChain.END_TOKEN);
        assertFalse(mc.hasNext());
        assertThrows(NoSuchElementException.class, () -> mc.next());
    }

    @Test
    public void testWalkStartWithNull() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "i want sleep";
        String sentence2 = "finish summer homework";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        assertThrows(IllegalArgumentException.class, () -> mc.reset(null));
    }

    @Test
    public void testWalkStartWithErrorSymbols() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "i want sleep";
        String sentence2 = "finish summer homework";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        mc.reset("hello");
        assertTrue(mc.hasNext());
        assertEquals("hello", mc.next());
        assertFalse(mc.hasNext());
    }
}