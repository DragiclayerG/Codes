package org.cis1200;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for FileLineIterator */
public class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void testHasText() {
        String words = "This is the only text.";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("This is the only text.", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testHasTextEmptyStringTexts() {
        String words = "";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertFalse(li.hasNext());
        assertThrows(NoSuchElementException.class, li::next);
    }

    @Test
    public void testNextEmptyStringTextsWithLines() {
        String words = "\n\n";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertThrows(NoSuchElementException.class, li::next);
    }

    @Test
    public void testBufferedReaderConstructorWithNull() {
        BufferedReader nullTest = null;
        assertThrows(IllegalArgumentException.class, () -> new
                     FileLineIterator(nullTest));
    }

    @Test
    public void testFilePathConstructorWithNull() {
        String nullTest = null;
        assertThrows(IllegalArgumentException.class, () -> new
                     FileLineIterator(nullTest));
    }

    @Test
    public void testFilePathConstructorEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new
                     FileLineIterator("Hello, World!"));
    }
}