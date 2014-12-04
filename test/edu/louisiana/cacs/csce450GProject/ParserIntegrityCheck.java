package edu.louisiana.cacs.csce450GProject;

import org.junit.Test;
import static org.junit.Assert.*; 
import static org.hamcrest.CoreMatchers.*;

public class ParserIntegrityCheck {

	@Test
	public void testSLRTableIntegrity() {
		
		SLRTable slrTable=new SLRTable();
		
		assertEquals(-7, slrTable.getSLRAction(1, '$'));
		assertEquals(-2, slrTable.getSLRAction(2, '+'));
	}
	
	@Test
	public void testParserIntegrityBasedOnFirstScript() {
		
		Parser parser=new Parser("data/test1.txt");
		parser.parse();
		
		assertEquals(10, parser.table.size());
		assertThat("Accept",is(equalTo(parser.table.get(parser.table.size()-1).aValue)));
		assertEquals("$",parser.pString);
		assertEquals("[E[E[T[F id]]]+[T[F id]]]",parser.pTStack.peek().getNodeRep());		
	}
	
	@Test
	public void testParserIntegrityBasedOnSecondScript() {
		
		Parser parser=new Parser("data/test2.txt");
		parser.parse();
		
		assertEquals(14, parser.table.size());
		assertThat("Accept",is(equalTo(parser.table.get(parser.table.size()-1).aValue)));
		assertThat("[1,$]",is(equalTo(parser.table.get(parser.table.size()-1).aLookup)));
		assertEquals("$",parser.pString);
		assertEquals("[E[E[T[T[F id]]*[F id]]]+[T[F id]]]",parser.pTStack.peek().getCompleteRep());
	}	

}
