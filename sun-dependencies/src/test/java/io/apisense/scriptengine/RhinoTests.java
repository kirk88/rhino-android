package io.apisense.scriptengine;

import com.script.rhino.RhinoScriptEngineFactory;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import com.script.ScriptEngine;
import com.script.ScriptEngineManager;
import com.script.ScriptException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RhinoTests {

    private ScriptEngineManager scriptEngineManager;

    @Before
    public void setUp() throws Exception {
        scriptEngineManager = new ScriptEngineManager();
    }

    @Test
    public void RhinoCanBeCreated() throws Exception {
        ScriptEngine rhino = new RhinoScriptEngineFactory().getScriptEngine();

        testEngine(rhino);
    }

    @Test
    public void RhinoCanBeRetrievedByEngineName() throws Exception {
        ScriptEngine rhino = scriptEngineManager.getEngineByName("mozilla.rhino");

        testEngine(rhino);
    }

    @Test
    public void RhinoCanBeRetrievedByShortName() throws Exception {
        ScriptEngine rhino = scriptEngineManager.getEngineByName("rhino");

        testEngine(rhino);
    }

    private void testEngine(ScriptEngine rhino) throws ScriptException {
        MatcherAssert.assertThat("Injected engine is not null", rhino, notNullValue());
        MatcherAssert.assertThat("We use rhino",
                rhino.getFactory().getEngineName().toLowerCase().contains("rhino"), is(true));
        MatcherAssert.assertThat("Injected engine is usable", (Double) rhino.eval("2.5+2"), is(4.5));
    }

    @Test
    public void testMathMethods() throws ScriptException {
        ScriptEngine rhino = scriptEngineManager.getEngineByName("rhino");
        testEngine(rhino);
        MatcherAssert.assertThat("We can access Math.asinh", (Double) rhino.eval("Math.asinh(0);"), is(0.0));
    }

}
