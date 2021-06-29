package com.example.foodstand;

import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
@LooperMode(LooperMode.Mode.PAUSED)
public class MainActivityUnitTest extends TestCase {

    public MainActivity mainActivity;

    @Before
    public void setup(){
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
    }

    @Test
    public void whenActivityStartsThenNoOrderIsThere(){
        Order order = mainActivity.currentOrder;
        assertNull(order);
        assert true; // really important :)

    }
    @Test
    public void whenActivityStartsThenTextsAreEmpty(){
        EditText textEdit = mainActivity.findViewById(R.id.editTextTextPersonName);
        EditText comments = mainActivity.findViewById(R.id.editTextComments);
        assertEquals("", textEdit.getText().toString());
        assertEquals("", comments.getText().toString());
    }

    @Test
    public void whenEditingTextThenTheyAreSaved(){
        EditText textEdit = mainActivity.findViewById(R.id.editTextTextPersonName);
        EditText comments = mainActivity.findViewById(R.id.editTextComments);
        textEdit.setText("hello");
        assertEquals("hello", textEdit.getText().toString());
        assertEquals("", comments.getText().toString());
    }

    @Test
    public void whenStartingActivityThenNoHummus(){
        Switch hummus = mainActivity.findViewById(R.id.switchHummus);
        Switch tahini = mainActivity.findViewById(R.id.switchTahini);
        assertFalse(hummus.isChecked());
        assertFalse(tahini.isChecked());
    }
}
