package edu.ucla.cs.model;

import org.junit.Test;


public class ViolationTest {

    @Test
    public void testGetViolationMessage() {
        Violation vio = new Violation(ViolationType.MissingStructure, 
                new APICall("write", "true", null, null, null));
        Pattern p = new Pattern(0, "FileChannel", "write", "TRY, write(ByteBuffer)@true, END_BLOCK, CATCH(IOException), END_BLOCK",
                        1231, true, "Handle the potential IOException thrown by write.", 10, 0, null);
        System.out.println(vio.getViolationMessage(p));
    }

}
