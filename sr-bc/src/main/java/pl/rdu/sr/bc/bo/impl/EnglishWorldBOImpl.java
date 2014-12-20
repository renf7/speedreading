package pl.rdu.sr.bc.bo.impl;

import static pl.rdu.sr.bc.Constant.ENG_WORDS_FILE;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pl.rdu.sr.bc.bo.EnglishWorldBO;

/**
 * Klasa do zarządzania angielskimi słowami.
 * 
 * @author rafalszymonduda@gmail.com
 *
 */
@Service
public class EnglishWorldBOImpl implements EnglishWorldBO {
    private static final Logger LOG = LoggerFactory.getLogger(EnglishWorldBOImpl.class);

    private List<String> words;

    private Random rand = new Random(System.currentTimeMillis());

    public EnglishWorldBOImpl() {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(ENG_WORDS_FILE);
            @SuppressWarnings("unchecked")
            List<String> readLines = IOUtils.readLines(fis);
            words = readLines;
        } catch (IOException ex) {
            LOG.error("Cannot read words", ex);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * Pobiera słowo o losowej kolejności.
     */
    public String getRandomWord() {
        int index = Math.abs(rand.nextInt() % words.size());
        return words.get(index);
    }

}
