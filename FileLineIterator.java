package org.cis1200;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * FileLineIterator provides a useful wrapper around Java's provided
 * BufferedReader and provides practice with implementing an Iterator. Your
 * solution should not read the entire file into memory at once, instead reading
 * a line whenever the next() method is called.
 * <p>
 * Note: Any IOExceptions thrown by readers should be caught and handled
 * properly. Do not use the ready() method from BufferedReader.
 */
public class FileLineIterator implements Iterator<String> {

    // Add the fields needed to implement your FileLineIterator

    private String nextText;
    private final BufferedReader readText;

    /**
     * Creates a FileLineIterator for the reader. Fill out the constructor so
     * that a user can instantiate a FileLineIterator. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * The only method that should be called on BufferedReader is readLine() and
     * close(). You cannot call any other methods.
     *
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */
    public FileLineIterator(BufferedReader reader) {
        boolean readerCheck = !(reader == null);
        if (readerCheck) {
            readText = reader;
            try {
                nextText = reader.readLine();
            } catch (IOException failRead) {
                nextText = null;
            }
        } else {
            throw new IllegalArgumentException("Error: The Text Read Is Null");
        }
    }

    /**
     * Creates a FileLineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * <p>
     * DO NOT MODIFY THIS METHOD.
     * 
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    /**
     * Takes in a filename and creates a BufferedReader.
     * See Java's documentation for BufferedReader to learn how to construct one
     * given a path to a file.
     *
     * @param filePath - the path to the CSV file to be turned to a
     *                 BufferedReader
     * @return a BufferedReader of the provided file contents
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public static BufferedReader fileToReader(String filePath) {
        boolean fileCheck = !(filePath == null);
        if (fileCheck) {
            try {
                FileReader readFile = new FileReader(filePath);
                BufferedReader checkRead = new BufferedReader(readFile);
                return checkRead;
            } catch (FileNotFoundException failRead) {
                throw new IllegalArgumentException("Error: File Read Not Found");
            }
        } else {
            throw new IllegalArgumentException("Error: The File Read Is Null");
        }
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        boolean emptyNext = !(nextText == null);
        if (emptyNext) {
            return emptyNext;
        }
        try {
            readText.close();
        } catch (IOException failRead) { }
        return emptyNext;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        String nextReading = nextText;
        boolean textHas = !hasNext();
        if (textHas) {
            throw new NoSuchElementException("Error: There Is No Data In The File");
        } else {
            try {
                nextText = readText.readLine();
            } catch (IOException failRead) {
                nextText = null;
            }
            return nextReading;
        }
    }
}