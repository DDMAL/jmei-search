package ca.mcgill.music.ddmal.meisearch;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;

public class ResponseTest {

    @Test

    public void test() {
        String query = "abc";
        String[] qparts = query.split("");
        String[] p2 = Arrays.copyOfRange(qparts, 1, qparts.length);
        assertThat(p2.length, Is.is(3));
        assertThat(p2[0], Is.is("a"));
        assertThat(p2[1], Is.is("b"));
        assertThat(p2[2], Is.is("c"));
    }

}
