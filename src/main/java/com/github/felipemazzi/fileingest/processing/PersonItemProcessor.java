package com.github.felipemazzi.fileingest.processing;

import org.springframework.batch.item.ItemProcessor;

import com.github.felipemazzi.fileingest.input.PersonInput;
import com.github.felipemazzi.fileingest.output.PersonOutput;

public class PersonItemProcessor implements ItemProcessor<PersonInput, PersonOutput> {

	@Override
	public PersonOutput process(PersonInput item) throws Exception {
		final int nameSplitIndex = item.getName().indexOf(" ");
		final int startIndex = 0;
		
		final String firstName;
		final String lastName;
		
		if (nameSplitIndex != -1) {
			firstName = item.getName().substring(startIndex, nameSplitIndex);
			lastName = item.getName().substring(nameSplitIndex).trim();
		} else {
			firstName = item.getName();
			lastName = "";
		}
		
		final Long phoneNumber = Long.parseLong(
				item.getPhoneNumber()
					.replace("(", "")
					.replace(")", "")
					.replace(" ", "")
					.replace("-", "")
				);
		
		final PersonOutput person = 
				new PersonOutput(firstName, lastName, phoneNumber, item.getEmailAddress());
		
		return person;
	}

}
