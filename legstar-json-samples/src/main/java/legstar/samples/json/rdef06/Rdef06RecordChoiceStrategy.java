package legstar.samples.json.rdef06;

import java.lang.reflect.Field;

import org.legstar.cobol.converter.CobolChoiceStrategy;

import legstar.samples.json.rdef06.Rdef06Record.OptlItemChoice;


public class Rdef06RecordChoiceStrategy implements CobolChoiceStrategy<Rdef06Record> {

	@Override
	public boolean choose(Rdef06Record rdef04Record, Object choice, Field alternative) {
		if (choice instanceof OptlItemChoice) {
			switch (alternative.getName()) {
			case "optlStruct":
				return true;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

}
