package legstar.samples.rdef04;

import java.lang.reflect.Field;

import org.legstar.cobol.type.converter.CobolConverterFromHostChoiceStrategy;

import legstar.samples.rdef04.Rdef04Record.OuterRedefinesLongChoice;
import legstar.samples.rdef04.Rdef04Record.OuterRedefinesLongChoice.OuterRedefinesShort.InnerRedefinesLongChoice;

public class Rdef04RecordChoiceStrategy implements CobolConverterFromHostChoiceStrategy<Rdef04Record> {

	@Override
	public boolean choose(Rdef04Record rdef04Record, Object choice, Field alternative) {
		if (choice instanceof OuterRedefinesLongChoice) {
			switch (alternative.getName()) {
			case "outerRedefinesShort":
				return true;
			default:
				return false;
			}
		} else if (choice instanceof InnerRedefinesLongChoice) {
			switch (alternative.getName()) {
			case "innerRedefinesShort":
				return true;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

}
