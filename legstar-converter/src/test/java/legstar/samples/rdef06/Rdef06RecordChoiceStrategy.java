package legstar.samples.rdef06;

import java.lang.reflect.Field;

import org.legstar.cobol.converter.CobolConverterFromHostChoiceStrategy;

import legstar.samples.rdef06.Rdef06Record.OptlItemChoice;


public class Rdef06RecordChoiceStrategy implements CobolConverterFromHostChoiceStrategy<Rdef06Record> {

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
