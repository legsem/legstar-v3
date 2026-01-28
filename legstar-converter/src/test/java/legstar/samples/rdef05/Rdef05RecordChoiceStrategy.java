package legstar.samples.rdef05;

import java.lang.reflect.Field;

import org.legstar.cobol.type.converter.CobolConverterFromHostChoiceStrategy;

import legstar.samples.rdef05.Rdef05Record.Choice1Alt1Choice;
import legstar.samples.rdef05.Rdef05Record.Choice2Alt1Choice;


public class Rdef05RecordChoiceStrategy implements CobolConverterFromHostChoiceStrategy<Rdef05Record> {

	@Override
	public boolean choose(Rdef05Record rdef04Record, Object choice, Field alternative) {
		if (choice instanceof Choice1Alt1Choice) {
			switch (alternative.getName()) {
			case "choice1Alt2":
				return true;
			default:
				return false;
			}
		} else if (choice instanceof Choice2Alt1Choice) {
			switch (alternative.getName()) {
			case "choice2Alt2":
				return true;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

}
