package com.example.group3.csci3130_group3_project;
import org.junit.*;
import org.junit.runners.Parameterized;
//import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorTest {
   passwordValidator v = new passwordValidator();

    //basciTest tests the rules given in stage 1 of the assignment
    @Test
    public void basicTest(){
        v.setPassword(""); //password is empty gives 0 points
        assertEquals(v.validate(), 0);
        v.setPassword("password"); //1 point for 8 or more chars
        assertEquals(v.validate(), 1);
        v.setPassword("PASSWORD"); //1 point for 8 or more chars
        v.setPassword("abc"); //1 point for not being "password"
        assertEquals(v.validate(), 1);
        v.setPassword("alonglongpassword"); //1 pt for length, one for not password
        assertEquals(v.validate(), 2);
    }

    /*Tests whether validate gives 1 point for numbers and characters, but not
     * for only numbers or only characters
     */
    @Test
    public void numberAndChar(){
        v.setPassword("33333333");
        assertEquals(v.validate(), 2);
        v.setPassword("EEEEEEEE");
        assertEquals(v.validate(), 2);
        v.setPassword("333E3333");
        assertEquals(v.validate(), 3);
    }

    /*specialChar tests whether validate awards a point for including
     *a special character. Shouldn't give extra points for additional special
     * characters
     */
    @Test
    public void specialChar(){
        char[] specials = {'!', '@', '#', '$', '%', '^', '&', '*', '(',
        ')', '_', '-', '+', '=', '<', '>', '?', '{', '[', '}', ']',
        '/', '~', '`', ':', ';'};
        v.setPassword("@eeeeeee");
        assertEquals(v.validate(), 3);
        v.setPassword("@#eeeeee");
        assertEquals(v.validate(), 3);
        for (int i = 0; i < specials.length; i++){
           String testString = "";
           testString += specials[i];
           v.setPassword(testString);
           assertEquals(v.validate(), 2);
       }
    }

    /* Tests whether the validator awards a point for having both upper and
     lower case letters.
     */
    @Test
    public void upperAndLower(){
        v.setPassword("eeeeeeee");
        assertEquals(v.validate(), 2);
        v.setPassword("EEEEEEEE");
        assertEquals(v.validate(), 2);
        v.setPassword("eeEeeeee");
        assertEquals(v.validate(), 3);
        v.setPassword("eEeEeEeEeE");
        assertEquals(v.validate(), 3);

    }

    /*Tests the method which displays the description of password strength*/
    @Test
    public void strengthDescribe(){
        v.setPassword("");
        assertEquals(v.describeStrength(), "Enter Password");
        v.setPassword("PASSWORD");
        assertEquals(v.describeStrength(), "Very Weak");
        v.setPassword("eeeeeeee");
        assertEquals(v.describeStrength(), "Weak");
        v.setPassword("eEeEeEeEeE");
        assertEquals(v.describeStrength(), "Moderate");
        v.setPassword("eEeEeE@eEeE");
        assertEquals(v.describeStrength(), "Strong");
        v.setPassword(("2eEeEeE@eeEe"));
        assertEquals(v.describeStrength(), "Very Strong");
    }

}
