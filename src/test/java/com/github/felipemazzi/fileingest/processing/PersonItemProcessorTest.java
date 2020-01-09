package com.github.felipemazzi.fileingest.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.felipemazzi.fileingest.input.PersonInput;
import com.github.felipemazzi.fileingest.output.PersonOutput;

public class PersonItemProcessorTest {

	private PersonItemProcessor subject;
	
	@Before
	public void setUp() {
		this.subject = new PersonItemProcessor();
	}
	
	@Test
	public void shouldReturnFirstAndLastNameOfAPerson() throws Exception {
		PersonInput input = new PersonInput("Felipe Mazzi", "123456789", "email@address");
		
		PersonOutput output = subject.process(input);
		
		assertEquals("Felipe", output.getFirstName());
		assertEquals("Mazzi", output.getLastName());
	}
}
