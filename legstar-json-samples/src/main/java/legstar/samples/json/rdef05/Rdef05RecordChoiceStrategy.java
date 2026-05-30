package legstar.samples.json.rdef05;

import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolFieldInfo;

import legstar.samples.json.rdef05.Rdef05Record.Choice1Alt1Choice;
import legstar.samples.json.rdef05.Rdef05Record.Choice2Alt1Choice;


public class Rdef05RecordChoiceStrategy implements CobolChoiceStrategy<Rdef05Record> {

	@Override
	public boolean choose(Rdef05Record rdef04Record, Object choice, CobolFieldInfo alternative) {
		if (choice instanceof Choice1Alt1Choice) {
			switch (alternative.name()) {
			case "choice1Alt2":
				return true;
			default:
				return false;
			}
		} else if (choice instanceof Choice2Alt1Choice) {
			switch (alternative.name()) {
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
